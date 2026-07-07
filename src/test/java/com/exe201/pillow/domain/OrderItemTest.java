package com.exe201.pillow.domain;

import static com.exe201.pillow.domain.CustomerOrderTestSamples.*;
import static com.exe201.pillow.domain.DefaultSizeTestSamples.*;
import static com.exe201.pillow.domain.OrderItemTestSamples.*;
import static com.exe201.pillow.domain.PillowTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.exe201.pillow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderItem.class);
        OrderItem orderItem1 = getOrderItemSample1();
        OrderItem orderItem2 = new OrderItem();
        assertThat(orderItem1).isNotEqualTo(orderItem2);

        orderItem2.setId(orderItem1.getId());
        assertThat(orderItem1).isEqualTo(orderItem2);

        orderItem2 = getOrderItemSample2();
        assertThat(orderItem1).isNotEqualTo(orderItem2);
    }

    @Test
    void pillowTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        Pillow pillowBack = getPillowRandomSampleGenerator();

        orderItem.setPillow(pillowBack);
        assertThat(orderItem.getPillow()).isEqualTo(pillowBack);

        orderItem.pillow(null);
        assertThat(orderItem.getPillow()).isNull();
    }

    @Test
    void defaultSizeTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        DefaultSize defaultSizeBack = getDefaultSizeRandomSampleGenerator();

        orderItem.setDefaultSize(defaultSizeBack);
        assertThat(orderItem.getDefaultSize()).isEqualTo(defaultSizeBack);

        orderItem.defaultSize(null);
        assertThat(orderItem.getDefaultSize()).isNull();
    }

    @Test
    void orderTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        CustomerOrder customerOrderBack = getCustomerOrderRandomSampleGenerator();

        orderItem.setOrder(customerOrderBack);
        assertThat(orderItem.getOrder()).isEqualTo(customerOrderBack);

        orderItem.order(null);
        assertThat(orderItem.getOrder()).isNull();
    }
}
