package com.raiink.order.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @description: 新建订单对象
 * @author: hulei
 * @create: 2020-05-13 21:51:16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "order_t")
@DynamicUpdate
@DynamicInsert
public class Order extends BaseInfo {
  /**
   * @description 订单待支付状态
   * @author hulei
   * @date 2020-05-17 19:39:47
   */
  public static String TO_BE_PAY = "to_be_pay";

  /**
   * @description 订单待创建
   * @author hulei
   * @date 2020-05-18 15:57:58
   */
  public static String TO_BE_CREATE = "to_be_create";

  @Id
  @GenericGenerator(name = "orderId", strategy = "guid")
  @GeneratedValue(generator = "orderId")
  @ApiModelProperty(hidden = true)
  private String id;

  @Column(columnDefinition = "varchar(32) unique comment '订单编号'")
  @ApiModelProperty(hidden = true)
  private String orderNo;

  @Column(columnDefinition = "varchar(20) not null comment '零售点编码'")
  private String outletNo;

  @Column(columnDefinition = "varchar(20) not null comment '订单种类编码'")
  private String orderClassNo;

  @Column(columnDefinition = "varchar(32) not null comment '客户编号'")
  private String customerNo;

  @Column(columnDefinition = "varchar(20) default 'init' comment '订单状态'")
  @ApiModelProperty(hidden = true)
  private String orderStatus;

  @Column(columnDefinition = "double default 0 comment '订单总额'")
  @ApiModelProperty(hidden = true)
  private double orderAmount;
}
