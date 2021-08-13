package com.github.hlam;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;

@KafkaListener(id = "classListener", topics = "topic2")
public class ClassListener {
    @KafkaHandler
    public void listen(SimpleData data) {
        System.out.println("Simple data. Little bit interesting");
    }

    @KafkaHandler
    public void listen(ImportantData data) {
        System.out.println("Very important data. Very interesting");
    }

    @KafkaHandler(isDefault = true)
    public void listenDefault(Object object) {
        System.out.println("Simple object. Nothing interesting");
    }
}
