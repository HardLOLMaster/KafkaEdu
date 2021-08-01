package com.github.hlam;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.AggregatingReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Collection;

@Component
public class AggregatingReplyingKafkaProducer implements IKafkaProducer {
    private final AggregatingReplyingKafkaTemplate<String, ImportantData, ImportantData> kafkaTemplate;

    @Autowired
    public AggregatingReplyingKafkaProducer(AggregatingReplyingKafkaTemplate<String, ImportantData, ImportantData> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendMessage(ImportantData data) {
        ProducerRecord<String, ImportantData> record = new ProducerRecord<>("replyRequestAggregate", data);
        RequestReplyFuture<String, ImportantData, Collection<ConsumerRecord<String, ImportantData>>> replyFuture = kafkaTemplate.sendAndReceive(record);
//        ConsumerRecord<String, Collection<ConsumerRecord<String, ImportantData>>> consumerRecord =
        replyFuture.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(@NotNull Throwable ex) {
                throw new RuntimeException(ex);
            }

            @Override
            public void onSuccess(ConsumerRecord<String, Collection<ConsumerRecord<String, ImportantData>>> consumerRecord) {
                Collection<ConsumerRecord<String, ImportantData>> value = consumerRecord.value();
                System.out.printf("REPLY topic: %s; value %s;%n", consumerRecord.topic(), consumerRecord.value().toString());
                for (ConsumerRecord<String, ImportantData> dataConsumerRecord : value) {
                    System.out.printf("REPLY topic: %s; value %s;%n", dataConsumerRecord.topic(), dataConsumerRecord.value().toString());
                }
            }
        });
    }
}
