package com.github.hlam;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class RandomDataUtils {
    private final static RandomDataGenerator randomDataGenerator = new RandomDataGenerator();

    @NotNull
    public static ImportantData getRandomImportantData() {
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

    @NotNull
    private static Date getRandomDate() {
        Calendar from = Calendar.getInstance();
        from.set(1900, Calendar.JANUARY, 1);

        Calendar to = Calendar.getInstance();
        to.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.DECEMBER, 31);
        LocalDate localDateFrom = LocalDateTime.ofInstant(from.toInstant(), from.getTimeZone().toZoneId()).toLocalDate();
        LocalDate localDateTo = LocalDateTime.ofInstant(to.toInstant(), to.getTimeZone().toZoneId()).toLocalDate();

        return Date.from(randomDate(localDateFrom, localDateTo).atTime(OffsetTime.now()).toInstant());
    }

    private static LocalDate randomDate(LocalDate startInclusive, LocalDate endExclusive) {
        long startEpochDay = startInclusive.toEpochDay();
        long endEpochDay = endExclusive.toEpochDay();
        long randomDay = ThreadLocalRandom
                .current()
                .nextLong(startEpochDay - 1, endEpochDay);

        return LocalDate.ofEpochDay(randomDay);
    }
}
