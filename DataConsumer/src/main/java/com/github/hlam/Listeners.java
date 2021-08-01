package com.github.hlam;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class Listeners {
    @KafkaListener(id = "listener1", topics = "replyRequest")
    @SendTo
    public ImportantData listener1(ImportantData importantData) {
        importantData.setFirstName("FirstName");
        importantData.setLastName("LastName");
        return importantData;
    }
}
