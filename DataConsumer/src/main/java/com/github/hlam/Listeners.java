package com.github.hlam;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
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

    @KafkaListener(id = "listener10", topicPartitions = {@TopicPartition(topic = "topic1", partitions = "0")})
    public ImportantData listener10(ConsumerRecord<String, ImportantData> record) {
        System.out.println("listener10 " + record.partition());
        return record.value();
    }

    @KafkaListener(id = "listener11", topicPartitions = {@TopicPartition(topic = "topic1", partitions = "1")})
    public ImportantData listener11(ConsumerRecord<String, ImportantData> record) {
        System.out.println("listener11 " + record.partition());
        return record.value();
    }

    private int next1 = 0;
    private int next2 = 0;

    @KafkaListener(id = "listener2", topics = "replyRequestAggregate")
    @SendTo
    public ImportantData listener2(ImportantData importantData) {
        if (next1++ < 5) {
            importantData.getSimpleData().setStringData("Next");
        } else {
            importantData.getSimpleData().setStringData("ImLast1");
            next1 = 0;
        }
        return importantData;
    }

    @KafkaListener(id = "listener3", topics = "replyRequestAggregate")
    @SendTo
    public ImportantData listener3(ImportantData importantData) {
        if (next2++ < 5) {
            importantData.getSimpleData().setStringData("Next");
        } else {
            importantData.getSimpleData().setStringData("ImLast");
            next2 = 0;
        }
        return importantData;
    }
}
