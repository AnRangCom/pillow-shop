package com.exe201.pillow.domain;

import static com.exe201.pillow.domain.CustomerOrderTestSamples.*;
import static com.exe201.pillow.domain.CustomerTestSamples.*;
import static com.exe201.pillow.domain.OrderItemTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.exe201.pillow.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CustomerOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerOrder.class);
        CustomerOrder customerOrder1 = getCustomerOrderSample1();
        CustomerOrder customerOrder2 = new CustomerOrder();
        assertThat(customerOrder1).isNotEqualTo(customerOrder2);

        customerOrder2.setId(customerOrder1.getId());
        assertThat(customerOrder1).isEqualTo(customerOrder2);

        customerOrder2 = getCustomerOrderSample2();
        assertThat(customerOrder1).isNotEqualTo(customerOrder2);
    }

    @Test
    void orderItemTest() {
        CustomerOrder customerOrder = getCustomerOrderRandomSampleGenerator();
        OrderItem orderItemBack = getOrderItemRandomSampleGenerator();

        customerOrder.addOrderItem(orderItemBack);
        assertThat(customerOrder.getOrderItems()).containsOnly(orderItemBack);
        assertThat(orderItemBack.getOrder()).isEqualTo(customerOrder);

        customerOrder.removeOrderItem(orderItemBack);
        assertThat(customerOrder.getOrderItems()).doesNotContain(orderItemBack);
        assertThat(orderItemBack.getOrder()).isNull();

        customerOrder.orderItems(new HashSet<>(Set.of(orderItemBack)));
        assertThat(customerOrder.getOrderItems()).containsOnly(orderItemBack);
        assertThat(orderItemBack.getOrder()).isEqualTo(customerOrder);

        customerOrder.setOrderItems(new HashSet<>());
        assertThat(customerOrder.getOrderItems()).doesNotContain(orderItemBack);
        assertThat(orderItemBack.getOrder()).isNull();
    }

    @Test
    void customerTest() {
        CustomerOrder customerOrder = getCustomerOrderRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        customerOrder.setCustomer(customerBack);
        assertThat(customerOrder.getCustomer()).isEqualTo(customerBack);

        customerOrder.customer(null);
        assertThat(customerOrder.getCustomer()).isNull();
    }
}
