package com.exe201.pillow.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PillowTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Pillow getPillowSample1() {
        return new Pillow().id(1L).name("name1").material("material1");
    }

    public static Pillow getPillowSample2() {
        return new Pillow().id(2L).name("name2").material("material2");
    }

    public static Pillow getPillowRandomSampleGenerator() {
        return new Pillow().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).material(UUID.randomUUID().toString());
    }
}
