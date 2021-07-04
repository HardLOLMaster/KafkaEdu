package com.github.hlam.raw;

import com.github.hlam.data.Person;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class MainNative
{
    private static final String TOPIC = "born_people";

    public static void main(String[] args)
    {
        final KafkaProducer<String, Person> kafkaProducer = initKafkaProducer();
        final KafkaConsumer<String, Person> kafkaConsumer = initKafkaConsumer();

        final Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Starting exit...");
            kafkaConsumer.wakeup();
            try
            {
                mainThread.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }));

        kafkaProducer.send(new ProducerRecord<>(TOPIC, new Person("Ivan")), (metadata, exception) -> {
            if (exception != null)
                exception.printStackTrace();
        });
        kafkaProducer.send(new ProducerRecord<>(TOPIC, new Person("Sergey")), (metadata, exception) -> {
            if (exception != null)
                exception.printStackTrace();
        });
        kafkaProducer.send(new ProducerRecord<>(TOPIC, new Person("Petr")), (metadata, exception) -> {
            if (exception != null)
                exception.printStackTrace();
        });
        kafkaConsumer.subscribe(Collections.singletonList(TOPIC));
        try
        {
            while (true)
            {
                ConsumerRecords<String, Person> records = kafkaConsumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, Person> record : records)
                {
                    Person value = record.value();
                    System.out.println("Today was born " + value.getName() + "!");
                }
                kafkaConsumer.commitAsync((offsets, exception) -> {
                    if (exception != null)
                        exception.printStackTrace();
                });
            }
        }
        catch (WakeupException ignored)
        {

        }
        finally
        {
            kafkaConsumer.close();
            kafkaProducer.close();
            System.out.println("******\nKafka Producer and Consumer closed\n******");
        }
    }

    private static KafkaProducer<String, Person> initKafkaProducer()
    {
        Properties producerProperties = new Properties();
        producerProperties.put("key.serializer", StringSerializer.class);
        producerProperties.put("value.serializer", PersonSerDe.class);
        producerProperties.put("bootstrap.servers", "localhost:9092");

        return new KafkaProducer<>(producerProperties);
    }

    private static KafkaConsumer<String, Person> initKafkaConsumer()
    {
        Properties consumerProperties = new Properties();
        consumerProperties.put("key.deserializer", StringDeserializer.class);
        consumerProperties.put("value.deserializer", PersonSerDe.class);
        consumerProperties.put("group.id", "logger");
        consumerProperties.put("bootstrap.servers", "localhost:9092");

        return new KafkaConsumer<>(consumerProperties);
    }
}
