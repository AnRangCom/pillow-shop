package com.exe201.pillow.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerOrderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static CustomerOrder getCustomerOrderSample1() {
        return new CustomerOrder().id(1L).orderCode("orderCode1");
    }

    public static CustomerOrder getCustomerOrderSample2() {
        return new CustomerOrder().id(2L).orderCode("orderCode2");
    }

    public static CustomerOrder getCustomerOrderRandomSampleGenerator() {
        return new CustomerOrder().id(longCount.incrementAndGet()).orderCode(UUID.randomUUID().toString());
    }
}
