package com.github.hlam.raw;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hlam.data.Person;
import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class PersonSerDe implements Serializer<Person>, Deserializer<Person>
{
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows @Override public byte[] serialize(String topic, Person data)
    {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(byteOutputStream, data);
        return byteOutputStream.toByteArray();
    }

    @SneakyThrows @Override public Person deserialize(String topic, byte[] data)
    {
        return objectMapper.readValue(data, Person.class);
    }

    @Override public void configure(Map<String, ?> configs, boolean isKey)
    {
        Serializer.super.configure(configs, isKey);
    }

    @Override public void close()
    {
        Serializer.super.close();
    }
}
