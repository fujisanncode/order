package com.raiink.order.dto;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 客户信息
 * @author: hulei
 * @create: 2020-05-13 21:58:39
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customer_t")
public class Customer extends BaseInfo {
  @Id
  @GenericGenerator(name = "customerId", strategy = "uuid")
  @GeneratedValue(generator = "customerId")
  private String Id;

  @Column(columnDefinition = "varchar(32) unique comment '客户编号'")
  private String customerNo;

  @Column(columnDefinition = "varchar(20) not null comment '客户电话'")
  private String customerPhone;

  @Column(columnDefinition = "varchar(300) not null comment '客户地址'")
  private String customerAddress;

  @Column(columnDefinition = "int default 0 comment '客户年龄'")
  private String customerAge;
}
