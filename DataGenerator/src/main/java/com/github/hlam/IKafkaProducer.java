package com.github.hlam;

import java.util.concurrent.ExecutionException;

public interface IKafkaProducer {
    void sendMessage(int count) throws ExecutionException, InterruptedException;
}
