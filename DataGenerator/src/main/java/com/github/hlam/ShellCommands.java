package com.github.hlam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.concurrent.ExecutionException;

@ShellComponent
public class ShellCommands {
    @Autowired
    public ShellCommands(DefaultKafkaProducer defaultKafkaProducer,
                         ReplyingKafkaProducer replyingKafkaProducer) {
        this.defaultKafkaProducer = defaultKafkaProducer;
        this.replyingKafkaProducer = replyingKafkaProducer;
    }

    private final DefaultKafkaProducer defaultKafkaProducer;
    private final ReplyingKafkaProducer replyingKafkaProducer;

    @ShellMethod(value = "Generate random data", key = "generate")
    public void generateRandomImportantData(@ShellOption(defaultValue = "default") String template,
                                            @ShellOption(defaultValue = "10") int count) throws ExecutionException, InterruptedException {
        IKafkaProducer kafkaProducer;
        switch (template) {
            case "reply":
                kafkaProducer = replyingKafkaProducer;
                break;
            case "default":
            default:
                kafkaProducer = defaultKafkaProducer;
                break;
        }
        for (int i = 0; i < count; i++) {
            ImportantData data = RandomDataUtils.getRandomImportantData();
            kafkaProducer.sendMessage(data);
        }
    }
}
