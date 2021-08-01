package com.github.hlam;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class LogConsumer {
    Logger logger = Logger.getLogger(LogConsumer.class.getName());

    @KafkaListener(topicPattern = ".*", groupId = "group_logger")
    public void consoleLogger(ConsumerRecord<String, Object> record) {
        String msg = "key: %s; topic %s; headers: %s; value: %s";
        logger.log(Level.INFO, String.format(msg, record.key(), record.topic(), record.headers(), record.value()));
    }
}
