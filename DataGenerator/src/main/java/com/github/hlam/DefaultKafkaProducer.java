package com.github.hlam;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

@Component
public class DefaultKafkaProducer implements IKafkaProducer {
    private final KafkaTemplate<String, ImportantData> kafkaTemplate;

    @Autowired
    public DefaultKafkaProducer(@Qualifier("dataKafkaTemplate") KafkaTemplate<String, ImportantData> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendMessage(int count) throws ExecutionException, InterruptedException {
        for (int i = 0; i < count; i++) {
            ImportantData data = RandomDataUtils.getRandomImportantData();
            ProducerRecord<String, ImportantData> record = new ProducerRecord<>("topic1", data);
            ListenableFuture<SendResult<String, ImportantData>> future = kafkaTemplate.send(record);
            System.out.print(future.get().getProducerRecord().value().toString());
        }
    }
}
