package com.raiink.order.configure;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

/**
 * @description: kafka发送消息回调方法
 * @author: hulei
 * @create: 2020-05-20 18:52:12
 */
@Slf4j
@Component
public class KafkaResultHandler<K, V> implements ProducerListener<K, V> {
  @Override
  public void onSuccess(ProducerRecord<K, V> producerRecord, RecordMetadata recordMetadata) {
    log.info("kafka send success");
  }

  @Override
  public void onError(ProducerRecord<K, V> producerRecord, Exception exception) {
    log.info("kafka send fail");
  }
}
