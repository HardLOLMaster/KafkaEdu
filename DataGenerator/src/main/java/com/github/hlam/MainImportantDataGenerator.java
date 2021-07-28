package com.github.hlam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class MainImportantDataGenerator {
    public static void main(String[] args) {
        SpringApplication.run(MainImportantDataGenerator.class);
    }
}
