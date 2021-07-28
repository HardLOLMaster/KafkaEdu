package com.github.hlam;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@EnableKafka
@SpringBootApplication
public class Main
{
    public static void main(String[] args)
    {
        SpringApplication.run(Main.class);
    }

    @Bean
    public ApplicationRunner applicationRunner(KafkaProducers kafkaProducers)
    {
        return args -> {
            RandomDataGenerator randomDataGenerator = new RandomDataGenerator();
            for (int i = 0; i < 1000; i++)
            {
                ImportantData data = getRandomData(randomDataGenerator);

                kafkaProducers.sendMessage(data);
            }
        };
    }

    @NotNull private ImportantData getRandomData(RandomDataGenerator randomDataGenerator)
    {
        ImportantData data = new ImportantData();
        data.setFirstName(RandomStringUtils.random(randomDataGenerator.nextInt(5, 10), true, false));
        data.setLastName(RandomStringUtils.random(randomDataGenerator.nextInt(5, 10), true, false));
        data.setBirthday(getRandomDate());
        SimpleData simpleData = new SimpleData();
        simpleData.setDateData(getRandomDate());
        simpleData.setLongData(randomDataGenerator.nextLong(0, 999));
        simpleData.setStringData(RandomStringUtils.random(10, true, false));
        data.setSimpleData(simpleData);
        return data;
    }

    @NotNull private Date getRandomDate()
    {
        Calendar from = Calendar.getInstance();
        from.set(1900, Calendar.JANUARY, 1);

        Calendar to = Calendar.getInstance();
        to.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.DECEMBER, 31);
        LocalDate localDateFrom = LocalDateTime.ofInstant(from.toInstant(), from.getTimeZone().toZoneId()).toLocalDate();
        LocalDate localDateTo = LocalDateTime.ofInstant(to.toInstant(), to.getTimeZone().toZoneId()).toLocalDate();

        return new Date(randomDate(localDateFrom, localDateTo).toEpochDay());
    }

    private LocalDate randomDate(LocalDate startInclusive, LocalDate endExclusive)
    {
        long startEpochDay = startInclusive.toEpochDay();
        long endEpochDay = endExclusive.toEpochDay();
        long randomDay = ThreadLocalRandom
                .current()
                .nextLong(startEpochDay - 1, endEpochDay);

        return LocalDate.ofEpochDay(randomDay);
    }
}
