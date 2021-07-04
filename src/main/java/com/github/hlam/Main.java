package com.github.hlam;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class Main
{
    public static void main(String[] args)
    {
        SpringApplication.run(Main.class);
    }

//    @Bean
//    public ApplicationRunner runner(KafkaProducers kafkaProducers)
//    {
//        return args -> {
//            kafkaProducers.sendSimpleMessage("key1");
//            kafkaProducers.sendSimpleMessage("key2");
//            kafkaProducers.sendSimpleMessage("key3");
//        };
//    }
}
