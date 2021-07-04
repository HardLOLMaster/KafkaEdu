package com.github.hlam;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@EmbeddedKafka(ports = 9092, partitions = 1, topics = "msg")
public class KafkaTest
{
    @Autowired
    private KafkaProducers kafkaProducers;
    @Autowired
    private KafkaConsumers kafkaConsumers;

    @Test
    public void testKafka() throws ExecutionException, InterruptedException
    {
        kafkaProducers.sendSimpleMessage("key1");
    }
}
