package com.exe201.pillow.domain;

import static com.exe201.pillow.domain.CustomerOrderTestSamples.*;
import static com.exe201.pillow.domain.CustomerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.exe201.pillow.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CustomerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = getCustomerSample1();
        Customer customer2 = new Customer();
        assertThat(customer1).isNotEqualTo(customer2);

        customer2.setId(customer1.getId());
        assertThat(customer1).isEqualTo(customer2);

        customer2 = getCustomerSample2();
        assertThat(customer1).isNotEqualTo(customer2);
    }

    @Test
    void orderTest() {
        Customer customer = getCustomerRandomSampleGenerator();
        CustomerOrder customerOrderBack = getCustomerOrderRandomSampleGenerator();

        customer.addOrder(customerOrderBack);
        assertThat(customer.getOrders()).containsOnly(customerOrderBack);
        assertThat(customerOrderBack.getCustomer()).isEqualTo(customer);

        customer.removeOrder(customerOrderBack);
        assertThat(customer.getOrders()).doesNotContain(customerOrderBack);
        assertThat(customerOrderBack.getCustomer()).isNull();

        customer.orders(new HashSet<>(Set.of(customerOrderBack)));
        assertThat(customer.getOrders()).containsOnly(customerOrderBack);
        assertThat(customerOrderBack.getCustomer()).isEqualTo(customer);

        customer.setOrders(new HashSet<>());
        assertThat(customer.getOrders()).doesNotContain(customerOrderBack);
        assertThat(customerOrderBack.getCustomer()).isNull();
    }
}
