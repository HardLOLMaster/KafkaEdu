package com.github.hlam;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class KafkaConsumers
{
    Logger logger = Logger.getLogger(KafkaConsumers.class.getName());

    @KafkaListener(topics = "msg", groupId = "group1_test_mega")
    public void listener1(ConsumerRecord<String, String> record)
    {
        String msg = "LISTENER 1 (key: %s, value: %s)";
        logger.log(Level.INFO, String.format(msg, record.key(), record.value()));
    }

    @KafkaListener(topics = "msg", groupId = "group2_test_mega")
    public void listener2(ConsumerRecord<String, String> record)
    {
        String msg = "LISTENER 2 (key: %s, value: %s)";
        logger.log(Level.INFO, String.format(msg, record.key(), record.value()));
    }
}
