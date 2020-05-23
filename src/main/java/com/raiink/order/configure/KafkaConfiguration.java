package com.raiink.order.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;

/**
 * @description: kafka配置
 * @author: hulei
 * @create: 2020-05-21 07:44:03
 */
@Configuration
public class KafkaConfiguration {
  @Autowired private ReadKafkaProperty readKafkaProperty;
  @Autowired private Environment environment;
  //
  // public Map getSenderConfig() {
  //  Map senderConfig =
  //      new HashMap() {
  //        {
  //          put(
  //              ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
  //              environment.getProperty("spring.kafka.bootstrap-servers"));
  //          put(
  //              ProducerConfig.RETRIES_CONFIG,
  //              environment.getProperty("spring.kafka.producer.retries"));
  //          put(
  //              ProducerConfig.BATCH_SIZE_CONFIG,
  //              environment.getProperty("spring.kafka.producer.batch-size"));
  //          put(
  //              ProducerConfig.BUFFER_MEMORY_CONFIG,
  //              environment.getProperty("spring.kafka.producer.buffer-memory"));
  //          put(
  //              ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
  //              environment.getProperty("spring.kafka.producer.key-serializer"));
  //          put(
  //              ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
  //              environment.getProperty("spring.kafka.producer.value-serializer"));
  //        }
  //      };
  //  return senderConfig;
  // }
  //
  // @Bean("myKafkaProducerFactory")
  // public ProducerFactory getProductFactory() {
  //  DefaultKafkaProducerFactory producerFactory =
  //      new DefaultKafkaProducerFactory(getSenderConfig());
  //  producerFactory.transactionCapable();
  //  producerFactory.setTransactionIdPrefix("kafka-tran-");
  //  return producerFactory;
  // }

  // 指定bean，解决ProducerFactory冲突的问题，配置事务管理器，通过@transational可配置kafka事务
  @Bean
  public KafkaTransactionManager getKafkaTransactionManager(ProducerFactory producerFactory) {
    KafkaTransactionManager kafkaTransactionManager = new KafkaTransactionManager(producerFactory);
    return kafkaTransactionManager;
  }
}
