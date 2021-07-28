package com.github.hlam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class ShellCommands {
    @Autowired
    public ShellCommands(KafkaProducers kafkaProducers) {
        this.kafkaProducers = kafkaProducers;
    }

    private final KafkaProducers kafkaProducers;

    @ShellMethod(value = "Generate random data", key = "generate")
    public void generateRandomImportantData(@ShellOption int count) {

        for (int i = 0; i < count; i++) {
            ImportantData data = RandomDataUtils.getRandomImportantData();

            kafkaProducers.sendMessage(data);
        }
    }
}
