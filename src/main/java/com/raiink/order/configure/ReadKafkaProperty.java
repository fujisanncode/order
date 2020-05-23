package com.raiink.order.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description: 注解读取properties
 * @author: hulei
 * @create: 2020-05-21 10:06:20
 */
@Data
@ConfigurationProperties("spring.kafka")
public class ReadKafkaProperty {
  private String bootstrapServer;
}
