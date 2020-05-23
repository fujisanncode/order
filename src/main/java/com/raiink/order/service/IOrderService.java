package com.raiink.order.service;

import com.raiink.order.dto.Order;

/**
 * @description: 订单服务
 * @author: hulei
 * @create: 2020-05-17 18:59:50
 */
public interface IOrderService {
  Order newOrder(Order order);
}
