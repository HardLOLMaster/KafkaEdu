package com.github.hlam.test;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainTest
{
    public static void main(String[] args)
    {
        SpringApplication.run(MainTest.class);
    }

    @Bean
    public ApplicationRunner runner(Producers kafkaProducers)
    {
        return args -> {
            kafkaProducers.sendMessage("key1", "Boris");
//            kafkaProducers.sendMessage("key2", "Ivan");
//            kafkaProducers.sendMessage("key3", "Pete");
        };
    }
}
