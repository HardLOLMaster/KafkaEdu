package com.github.hlam;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.BatchLoggingErrorHandler;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.LoggingErrorHandler;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProducerConfig {
    public Map<String, Object> producerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, ImportantData> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean("DefaultKafkaTemplate")
    public KafkaTemplate<String, ImportantData> dataKafkaTemplate(ProducerFactory<String, ImportantData> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean("ReplyingKafkaTemplate")
    public ReplyingKafkaTemplate<String, ImportantData, ImportantData> replyingKafkaTemplate(
            ProducerFactory<String, ImportantData> producerFactory,
            ConcurrentMessageListenerContainer<String, ImportantData> repliesContainer) {
        ReplyingKafkaTemplate<String, ImportantData, ImportantData> replyingKafkaTemplate = new ReplyingKafkaTemplate<>(producerFactory, repliesContainer);
        replyingKafkaTemplate.setSharedReplyTopic(true);
        return replyingKafkaTemplate;
    }

    @Bean
    public ConsumerFactory<String, ImportantData> consumerFactory() {
        JsonDeserializer<ImportantData> deserializer = new JsonDeserializer<>();
        deserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(consumerConfig(),
                new StringDeserializer(),
                deserializer);
    }

    public Map<String, Object> consumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        return props;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ImportantData> kafkaListenerContainerFactory(ConsumerFactory<String, ImportantData> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, ImportantData> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setErrorHandler(new LoggingErrorHandler());
        return factory;
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, ImportantData> repliesContainer(
            ConcurrentKafkaListenerContainerFactory<String, ImportantData> containerFactory) {

        ConcurrentMessageListenerContainer<String, ImportantData> repliesContainer =
                containerFactory.createContainer("replyResponse");
        repliesContainer.getContainerProperties().setGroupId("repliesGroup");
        repliesContainer.setAutoStartup(false);
        repliesContainer.setBatchErrorHandler(new BatchLoggingErrorHandler());
        return repliesContainer;
    }
}
