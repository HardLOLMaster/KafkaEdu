package com.github.hlam.test;

import com.github.hlam.data.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

@Component
public class Producers
{
    private final KafkaTemplate<String, Person> kafkaTemplate;

    @Autowired
    public Producers(KafkaTemplate<String, Person> kafkaTemplate)
    {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String key, String name) throws ExecutionException, InterruptedException
    {
        Person person = new Person(name);
        ListenableFuture<SendResult<String, Person>> send = kafkaTemplate.send("born_people", key, person);
        SendResult<String, Person> x = send.get();
        System.out.println(x);
    }
}
