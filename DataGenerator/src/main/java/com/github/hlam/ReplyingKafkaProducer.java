package com.github.hlam;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@Component
public class ReplyingKafkaProducer implements IKafkaProducer {
    private final Logger LOGGER = Logger.getLogger(ReplyingKafkaProducer.class.getName());
    private final ReplyingKafkaTemplate<String, ImportantData, ImportantData> kafkaTemplate;

    @Autowired
    public ReplyingKafkaProducer(
            @Qualifier("ReplyingKafkaTemplate") ReplyingKafkaTemplate<String, ImportantData, ImportantData> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendMessage(ImportantData data) throws ExecutionException, InterruptedException {
        ProducerRecord<String, ImportantData> record = new ProducerRecord<>("replyRequest", data);
        RequestReplyFuture<String, ImportantData, ImportantData> replyFuture = kafkaTemplate.sendAndReceive(record);
//        replyFuture.getSendFuture().addCallback(result -> {
//                    assert result != null;
//                    LOGGER.log(Level.INFO, result.getProducerRecord().value().toString());
//                },
//                ex -> LOGGER.log(Level.WARNING, ex.getMessage()));
        ConsumerRecord<String, ImportantData> consumerRecord = replyFuture.get();
        System.out.printf("REPLY topic: %s; value %s;%n", consumerRecord.topic(), consumerRecord.value().toString());
    }
}
