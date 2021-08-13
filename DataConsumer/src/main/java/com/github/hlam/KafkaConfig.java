package com.github.hlam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class KafkaConfig implements KafkaListenerConfigurer {
    private final LocalValidatorFactoryBean validator;

    @Autowired
    public KafkaConfig(LocalValidatorFactoryBean validator) {
        this.validator = validator;
    }

    @Override
    public void configureKafkaListeners(KafkaListenerEndpointRegistrar registrar) {
        registrar.setValidator(this.validator);

    }

    @Bean
    public KafkaListenerErrorHandler validationErrorHandler() {
        return (m, e) -> {
            System.out.println("Sorry for validation error; " + e.getMessage());
            return null; //ignoring while listener don't have @SendTo
        };
    }
}
