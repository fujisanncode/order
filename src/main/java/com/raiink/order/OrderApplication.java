package com.raiink.order;

import com.raiink.order.configure.ReadKafkaProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableConfigurationProperties(ReadKafkaProperty.class) // 使用configurationProperties注解, 并指定类
@EnableAspectJAutoProxy(
    exposeProxy = true,
    proxyTargetClass = true) // 切面代理自动配置(切面允许被访问，使用cglib代理而不是jdk)
@EnableFeignClients(basePackages = {"com.raiink.feignapi.api"}) // 指定需要扫描哪些包下面的 @FeignClient注解
@EnableEurekaClient
// 指定扫描feign所在包，指定扫描当前启动类所在包
@ComponentScan(basePackages = {"com.raiink.feignapi.api", "com.raiink.order", "org.dromara.hmily"})
@SpringBootApplication(
    exclude = {MongoAutoConfiguration.class}) // @configuration + @EnableConfigure +　@componentScan
public class OrderApplication {

  public static void main(String[] args) {
    SpringApplication.run(OrderApplication.class, args);
  }
}
