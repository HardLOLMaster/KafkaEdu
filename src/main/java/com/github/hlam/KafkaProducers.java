package com.github.hlam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

@Component
public class KafkaProducers
{
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducers(KafkaTemplate<String, String> kafkaTemplate)
    {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendSimpleMessage(String key) throws ExecutionException, InterruptedException
    {
        ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send("msg", key, "TEST MESSAGE");
        SendResult<String, String> x = send.get();
        System.out.println(x);
    }
}
