package com.github.hlam;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.BatchLoggingErrorHandler;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.LoggingErrorHandler;
import org.springframework.kafka.requestreply.AggregatingReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Collection;
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

    @Bean
    public KafkaTemplate<String, ImportantData> dataKafkaTemplate(ProducerFactory<String, ImportantData> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ReplyingKafkaTemplate<String, ImportantData, ImportantData> replyingKafkaTemplate(
            ProducerFactory<String, ImportantData> producerFactory,
            ConcurrentMessageListenerContainer<String, ImportantData> repliesContainer) {
        ReplyingKafkaTemplate<String, ImportantData, ImportantData> replyingKafkaTemplate = new ReplyingKafkaTemplate<>(producerFactory, repliesContainer);
        replyingKafkaTemplate.setSharedReplyTopic(true);
        return replyingKafkaTemplate;
    }

    @Bean
    public AggregatingReplyingKafkaTemplate<String, ImportantData, ImportantData> aggregatingReplyingKafkaTemplate(
            ProducerFactory<String, ImportantData> producerFactory,
            ConcurrentMessageListenerContainer<String, Collection<ConsumerRecord<String, ImportantData>>> repliesContainer) {
        AggregatingReplyingKafkaTemplate<String, ImportantData, ImportantData> replyingKafkaTemplate =
                new AggregatingReplyingKafkaTemplate<>(producerFactory,
                        repliesContainer,
                        (consumerRecords, bool) -> {
                            System.out.println("RECORDS IN = " + consumerRecords.size());
                            System.out.println("bool = " + bool);
//                            return bool; //condition for exit if timeout
                            boolean result = false;
//                            if(consumerRecords.size()==2){// condition for exit
//                                consumerRecords.remove(1);
//                                return true;
//                            }
                            for (ConsumerRecord<String, ImportantData> consumerRecord : consumerRecords) {
                                ImportantData value = consumerRecord.value();
                                SimpleData simpleData = value.getSimpleData();
                                result = "ImLast".equalsIgnoreCase(simpleData.getStringData());
                                System.out.println("simpleDate = " + value.getSimpleData().getStringData() + "; result = " + result);
                            }
                            return result;
//                            return consumerRecords.size()==2; //condition for exit
                        });
        replyingKafkaTemplate.setSharedReplyTopic(true);
        replyingKafkaTemplate.setReturnPartialOnTimeout(false); //exit by no timeout
//        replyingKafkaTemplate.setReturnPartialOnTimeout(true); // exit by timeout
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

    @Bean
    public ConsumerFactory<String, Collection<ConsumerRecord<String, ImportantData>>> consumerFactoryCollection() {
        JsonDeserializer<Collection<ConsumerRecord<String, ImportantData>>> deserializer = new JsonDeserializer<>();
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
    public ConcurrentKafkaListenerContainerFactory<String, Collection<ConsumerRecord<String, ImportantData>>>
    kafkaListenerContainerFactoryCollection(ConsumerFactory<String, Collection<ConsumerRecord<String, ImportantData>>> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Collection<ConsumerRecord<String, ImportantData>>> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
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

    @Bean
    public ConcurrentMessageListenerContainer<String, Collection<ConsumerRecord<String, ImportantData>>> repliesContainerCollection(
            ConcurrentKafkaListenerContainerFactory<String, Collection<ConsumerRecord<String, ImportantData>>> containerFactory) {
        ContainerProperties containerProperties = containerFactory.getContainerProperties();
        containerProperties.getKafkaConsumerProperties().setProperty("enable.auto.commit", "false");
        ConcurrentMessageListenerContainer<String, Collection<ConsumerRecord<String, ImportantData>>> repliesContainer =
                containerFactory.createContainer("replyResponseAggregate");
        repliesContainer.getContainerProperties().setGroupId("repliesGroupAggregate");
        repliesContainer.setAutoStartup(false);
        repliesContainer.setBatchErrorHandler(new BatchLoggingErrorHandler());
        repliesContainer.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return repliesContainer;
    }
}
