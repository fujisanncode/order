package com.raiink.order.service.impl;

import com.raiink.feignapi.api.InvApi;
import com.raiink.order.dto.Order;
import com.raiink.order.hmily.domain.CancelLog;
import com.raiink.order.hmily.domain.ConfirmLog;
import com.raiink.order.hmily.domain.TryLog;
import com.raiink.order.hmily.repository.CancelRepository;
import com.raiink.order.hmily.repository.ConfirmRepository;
import com.raiink.order.hmily.repository.TryRepository;
import com.raiink.order.repository.OrderRepository;
import com.raiink.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.Hmily;
import org.dromara.hmily.common.exception.HmilyException;
import org.dromara.hmily.core.concurrent.threadlocal.HmilyTransactionContextLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @description: 订单服务
 * @author: hulei
 * @create: 2020-05-17 19:00:41
 */
@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {
  @Autowired private OrderRepository orderRepository;
  @Autowired private TryRepository tryRepository;
  @Autowired private ConfirmRepository confirmRepository;
  @Autowired private CancelRepository cancelRepository;
  @Autowired private InvApi invApi;
  @Autowired private ApplicationContext applicationContext;

  @Transactional
  @Override
  public Order newOrder(Order order) {
    OrderServiceImpl orderService = applicationContext.getBean(OrderServiceImpl.class);
    orderService.tryCreateOrder(order);
    return null;
  }

  // try, 准备订单（订单设置为建立中）
  @Hmily(confirmMethod = "commitCreateOrder", cancelMethod = "cancelCreateOrder")
  @Transactional
  public void tryCreateOrder(Order order) {
    String tranId = HmilyTransactionContextLocal.getInstance().get().getTransId();
    log.info("start hmily transaction: {}", tranId);
    // step1-1: 校验接口幂等（根据事务id，如果存在try记录，则跳过）
    if (tryRepository.existsByTxNo(tranId)) {
      log.info("{} -> 库存服务，try已经执行，保证接口幂等，不重复执行try", tranId);
      return;
    }
    // step1-2: 悬挂处理，如果confirm或者cancel已经执行过了（证明try肯定在执行中），则不重复执行try
    if (confirmRepository.existsByTxNo(tranId) || cancelRepository.existsByTxNo(tranId)) {
      log.info("{} -> 库存服务，confirm或者cancel已经执行，try在悬挂中，不重复执行try", tranId);
      return;
    }
    // step2-1: try，尝试锁定订单(创建订单并设置状态为创建中)
    order.setOrderStatus(Order.TO_BE_CREATE);
    order.setOrderNo(generatorNewOrderNo(order));
    order.setOrderAmount(100); // 调用库存系统计算价格
    if (orderRepository.save(order) == null) {
      throw new HmilyException("try-订单设置为创建中失败");
    }
    // step2-2: try 库存服务，如果库存服务失败，抛出异常
    if (!invApi.preDeductInv("S001G0005", 2)) {
      throw new HmilyException("try-预扣库存失败");
    }
    // step3-1: try结束后，try日志写入表中
    TryLog tryLog = new TryLog();
    tryLog.setTxNo(tranId);
    tryRepository.save(tryLog);
  }

  /**
   * @description 查询数据库或者redis生成递增流水号
   * @return java.lang.String
   * @author hulei
   * @date 2020-05-17 19:49:32
   */
  private String generatorNewOrderNo(Order order) {
    String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYMMdd"));
    Integer countOrder =
        orderRepository.countOrderByCondition(order.getOutletNo(), order.getOrderClassNo(), now)
            + 1;
    NumberFormat numberFormat = NumberFormat.getInstance();
    numberFormat.setMinimumIntegerDigits(5);
    numberFormat.setGroupingUsed(false); // 不需要这种逗号 111,111,111
    return order
        .getOutletNo()
        .concat(order.getOrderClassNo())
        .concat(now)
        .concat(numberFormat.format(countOrder));
  }

  // confirm, 提交订单
  @Transactional
  public void commitCreateOrder(Order order) {
    String tranId = HmilyTransactionContextLocal.getInstance().get().getTransId();
    // step1-1: 验证confirm接口幂等
    if (confirmRepository.existsByTxNo(tranId)) {
      log.info("验证confirm幂等 -> {}", tranId);
      return;
    }
    // step2-1: confirm或者cancel中失败只能重试或者人工干涉
    orderRepository.updateOrOrderStatus(order.getOrderStatus(), order.getOrderNo());
    // step3-1: 写confirm日志
    ConfirmLog confirmLog = new ConfirmLog();
    confirmLog.setTxNo(tranId);
    confirmRepository.save(confirmLog);
  }

  // cancel, 取消订单
  @Transactional
  public void cancelCreateOrder(Order order) {
    String tranId = HmilyTransactionContextLocal.getInstance().get().getTransId();
    // step1-1: 验证接口幂等
    if (cancelRepository.existsByTxNo(tranId)) {
      log.info("cancel接口验证幂等: {}", tranId);
      return;
    }
    // step1-2：处理空回滚情况，try中没有操作，不需要执行回滚操作
    if (!tryRepository.existsByTxNo(tranId)) {
      log.info("cancel阶段出现空回滚的情况: {}", tranId);
      return;
    }
    // step2-1: 订单重置创建中状态
    orderRepository.updateOrOrderStatus(Order.TO_BE_CREATE, order.getOrderNo());
    // step3-1: 写回滚日志
    CancelLog cancelLog = new CancelLog();
    cancelLog.setTxNo(tranId);
    cancelRepository.save(cancelLog);
  }
}
