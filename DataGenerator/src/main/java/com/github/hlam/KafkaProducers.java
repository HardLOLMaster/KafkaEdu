package com.github.hlam;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducers
{
    private final KafkaTemplate<String, ImportantData> kafkaTemplate;

    @Autowired
    public KafkaProducers(KafkaTemplate<String, ImportantData> kafkaTemplate)
    {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(ImportantData data)
    {
        ProducerRecord<String, ImportantData> record = new ProducerRecord<>("topic1", data);
        kafkaTemplate.execute(producer -> producer.send(record, (metadata, exception) -> {
            if (exception != null)
            {
                exception.printStackTrace();
            }
        }));
    }
}
