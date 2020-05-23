package com.raiink.order.hmily.domain;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @description: tcc-confirm日志
 * @author: hulei
 * @create: 2020-05-18 18:22:35
 */
@Data
@Entity
@Table(name = "confirm_log_t")
@DynamicInsert
@DynamicUpdate
public class ConfirmLog {
  @Id
  @GenericGenerator(name = "confirm_log_id", strategy = "uuid")
  @GeneratedValue(generator = "confirm_log_id")
  private String id;

  @Column(columnDefinition = "varchar(64) unique comment '事务编号'")
  private String txNo;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(columnDefinition = "timestamp default now() comment '写日志时间'")
  private Date createTime;
}
