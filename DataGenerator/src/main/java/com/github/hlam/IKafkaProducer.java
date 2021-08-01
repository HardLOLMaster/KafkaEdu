package com.github.hlam;

import java.util.concurrent.ExecutionException;

public interface IKafkaProducer {
    void sendMessage(ImportantData data) throws ExecutionException, InterruptedException;
}
