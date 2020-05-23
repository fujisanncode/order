package com.raiink.order.repository;

import com.raiink.order.dto.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * @description: 订单
 * @author: hulei
 * @create: 2020-05-13 22:43:26
 */
public interface OrderRepository extends CrudRepository<Order, String> {
  @Query(
      value =
          "select count(1) from test_order_s.order_t o where o.outlet_no = ?1 and o.order_class_no = ?2"
              + " and to_days(o.create_time) = to_days(str_to_date(?3, '%y%m%d'))",
      nativeQuery = true)
  Integer countOrderByCondition(String outletNo, String orderClassNo, String date);

  @Modifying
  @Query(
      value = "update test_order_s.order_t od set od.order_status = ?1 where od.order_no = ?2 ",
      nativeQuery = true)
  Integer updateOrOrderStatus(String toStatus, String orderNo);
}
