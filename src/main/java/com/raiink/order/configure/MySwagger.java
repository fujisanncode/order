package com.raiink.order.configure;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @description: swagger配置
 * @author: hulei
 * @create: 2020-05-15 19:52:25
 */
@EnableSwagger2 // 开启swagger，必须扫描到此包
@Configuration
public class MySwagger {
  @Bean
  public Docket runSwagger() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(myApiInfo())
        .select()
        .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
        .build();
  }

  private ApiInfo myApiInfo() {
    return new ApiInfoBuilder()
        // .contact("hulei")
        .title("订单系统的Swagger文档")
        .description("hulei market，订单系统")
        .version("v1.0")
        .build();
  }
}
