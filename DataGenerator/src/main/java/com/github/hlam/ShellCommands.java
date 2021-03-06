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
                         ReplyingKafkaProducer replyingKafkaProducer,
                         AggregatingReplyingKafkaProducer aggregatingReplyingKafkaProducer) {
        this.defaultKafkaProducer = defaultKafkaProducer;
        this.replyingKafkaProducer = replyingKafkaProducer;
        this.aggregatingReplyingKafkaProducer = aggregatingReplyingKafkaProducer;
    }

    private final DefaultKafkaProducer defaultKafkaProducer;
    private final ReplyingKafkaProducer replyingKafkaProducer;
    private final AggregatingReplyingKafkaProducer aggregatingReplyingKafkaProducer;

    @ShellMethod(value = "Generate random data", key = "generate")
    public void generateRandomImportantData(@ShellOption(defaultValue = "default") String template,
                                            @ShellOption(defaultValue = "10") int count) throws ExecutionException, InterruptedException {
        IKafkaProducer kafkaProducer;
        switch (template) {
            case "reply":
                kafkaProducer = replyingKafkaProducer;
                break;
            case "aggregate":
                kafkaProducer = aggregatingReplyingKafkaProducer;
                break;
            case "default":
            default:
                kafkaProducer = defaultKafkaProducer;
                break;
        }
        kafkaProducer.sendMessage(count);
    }
}
