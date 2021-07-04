package com.github.hlam.test;

import com.github.hlam.data.Person;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class Listeners
{
    Logger logger = Logger.getLogger(Listeners.class.getName());

    @KafkaListener(topics = "born_people", groupId = "born_group")
    @SendTo("email_assignment")
    public Person born(ConsumerRecord<String, Person> record)
    {
        String msg = "BORN (key: %s, value: %s)";
        Person person = record.value();
        person.setYear(2000);
        logger.log(Level.INFO, String.format(msg, record.key(), person));
        return person;
    }

    @KafkaListener(topics = "email_assignment", groupId = "email_group")
    @SendTo("normal_people")
    public Person email(ConsumerRecord<String, Person> record)
    {
        String msg = "EMAIL (key: %s, value: %s)";
        Person person = record.value();
        person.setEmail(person.getName().toLowerCase() + "_" + person.getYear() + "@mail.com");
        logger.log(Level.INFO, String.format(msg, record.key(), person));
        return person;
    }

    @KafkaListener(topics = "normal_people", groupId = "normal_group")
    public void normalPeople(ConsumerRecord<String, Person> record)
    {
        String msg = "NORMAL PEOPLE (key: %s, value: %s)";
        logger.log(Level.INFO, String.format(msg, record.key(), record.value()));
    }

    @KafkaListener(topicPattern = ".*", groupId = "logger")
    public void log(ConsumerRecord<String, Person> record)
    {
        String msg = "LOGGER (key: %s, value: %s)";
        logger.log(Level.INFO, String.format(msg, record.key(), record.value()));
    }
}
