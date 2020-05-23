package com.raiink.order.controller;

import com.raiink.feignapi.api.InvApi;
import com.raiink.order.configure.KafkaResultHandler;
import com.raiink.order.dto.Order;
import com.raiink.order.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @description: 创建订单
 * @author: hulei
 * @create: 2020-05-13 21:46:17
 */
@Api(value = "create order", tags = "订单服务")
@RestController // 等同于controller+responseBody
@RequestMapping("/create")
public class OrderController {
  @Autowired private IOrderService iOrderService;

  @Autowired private InvApi invApi;

  @Autowired private KafkaTemplate kafkaTemplate;

  @Autowired private KafkaResultHandler kafkaResultHandler;

  @Transactional
  @ApiOperation(value = "new order", notes = "ttc分布式事务，新建订单")
  @PostMapping("/new-order-ttc")
  public Order newOrder(@RequestBody Order order) {
    Order orderResult = iOrderService.newOrder(order);
    return orderResult;
  }

  @Transactional
  @ApiImplicitParams({@ApiImplicitParam(name = "hello", defaultValue = "hellos-msg")})
  @ApiOperation(value = "hello-mq-kafka-send", notes = "测试kafka发送消息")
  @GetMapping("/hello-mq-kafka-send/{hello}")
  public void helloMqKafka(@PathVariable String hello) {
    // 设置发送消息的回调
    kafkaTemplate.setProducerListener(kafkaResultHandler);
    // 默认为异步发送消息， .get() 获取future返回值，让发消息成为同步
    kafkaTemplate.send("test-topic", UUID.randomUUID().toString(), hello);
    // 测试kafka事务回滚
    // throw new RuntimeException("test kafka transaction");
  }

  @ApiOperation(value = "hello-mq-kafka-send2", notes = "不通过kafka事务管理器和事务注解控制事务")
  @ApiImplicitParams({@ApiImplicitParam(name = "hello", defaultValue = "hellos-msg")})
  @GetMapping("/hello-mq-kafka-send2/{hello}")
  public void helloMqKafka2(@PathVariable String hello) {
    kafkaTemplate.executeInTransaction(
        new KafkaOperations.OperationsCallback() {
          @Override
          public Object doInOperations(KafkaOperations kafkaOperations) {
            // 设置发送消息的回调
            kafkaTemplate.setProducerListener(kafkaResultHandler);
            // 默认为异步发送消息， .get() 获取future返回值，让发消息成为同步
            kafkaTemplate.send("test-topic", 0, UUID.randomUUID().toString(), hello);
            kafkaTemplate.send("test-topic", 1, UUID.randomUUID().toString(), hello);
            kafkaTemplate.send("test-topic", 2, UUID.randomUUID().toString(), hello);
            kafkaTemplate.send("test-topic", 3, UUID.randomUUID().toString(), hello);
            // 测试kafka事务回滚
            // throw new RuntimeException("test kafka transaction");
            return null;
          }
        });
  }
}
