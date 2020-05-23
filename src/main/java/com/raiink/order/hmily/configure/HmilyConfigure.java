package com.raiink.order.hmily.configure;

import org.dromara.hmily.common.config.HmilyDbConfig;
import org.dromara.hmily.core.bootstrap.HmilyTransactionBootstrap;
import org.dromara.hmily.core.service.HmilyInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @description: hmily配置
 * @author: hulei
 * @create: 2020-05-18 18:42:36
 */
@Configuration
public class HmilyConfigure {
  @Autowired private Environment env;

  @Bean
  public HmilyTransactionBootstrap hmilyTransactionBootstrap(HmilyInitService hmilyInitService) {
    HmilyTransactionBootstrap hmilyTransactionBootstrap =
        new HmilyTransactionBootstrap(hmilyInitService);
    hmilyTransactionBootstrap.setSerializer(env.getProperty("org.dromara.hmily.serializer"));
    hmilyTransactionBootstrap.setRecoverDelayTime(
        Integer.parseInt(env.getProperty("org.dromara.hmily.recoverDelayTime")));
    hmilyTransactionBootstrap.setRetryMax(
        Integer.parseInt(env.getProperty("org.dromara.hmily.retryMax")));
    hmilyTransactionBootstrap.setScheduledDelay(
        Integer.parseInt(env.getProperty("org.dromara.hmily.scheduledDelay")));
    hmilyTransactionBootstrap.setScheduledThreadMax(
        Integer.parseInt(env.getProperty("org.dromara.hmily.scheduledThreadMax")));
    hmilyTransactionBootstrap.setRepositorySupport(
        env.getProperty("org.dromara.hmily.repositorySupport"));
    hmilyTransactionBootstrap.setStarted(
        Boolean.parseBoolean(env.getProperty("org.dromara.hmily.started")));
    HmilyDbConfig hmilyDbConfig = new HmilyDbConfig();
    hmilyDbConfig.setDriverClassName(
        env.getProperty("org.dromara.hmily.hmilyDbConfig.driverClassName"));
    hmilyDbConfig.setUrl(env.getProperty("org.dromara.hmily.hmilyDbConfig.url"));
    hmilyDbConfig.setUsername(env.getProperty("org.dromara.hmily.hmilyDbConfig.username"));
    hmilyDbConfig.setPassword(env.getProperty("org.dromara.hmily.hmilyDbConfig.password"));
    hmilyTransactionBootstrap.setHmilyDbConfig(hmilyDbConfig);
    return hmilyTransactionBootstrap;
  }
}
