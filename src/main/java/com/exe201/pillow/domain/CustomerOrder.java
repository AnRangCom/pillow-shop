package com.exe201.pillow.domain;

import com.exe201.pillow.domain.enumeration.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CustomerOrder.
 */
@Entity
@Table(name = "customer_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerOrder implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "order_code", nullable = false, unique = true)
    private String orderCode;

    @NotNull
    @Column(name = "order_date", nullable = false)
    private Instant orderDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pillow", "defaultSize", "order" }, allowSetters = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "orders" }, allowSetters = true)
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CustomerOrder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderCode() {
        return this.orderCode;
    }

    public CustomerOrder orderCode(String orderCode) {
        this.setOrderCode(orderCode);
        return this;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public CustomerOrder orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public CustomerOrder status(OrderStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public CustomerOrder totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Set<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        if (this.orderItems != null) {
            this.orderItems.forEach(i -> i.setOrder(null));
        }
        if (orderItems != null) {
            orderItems.forEach(i -> i.setOrder(this));
        }
        this.orderItems = orderItems;
    }

    public CustomerOrder orderItems(Set<OrderItem> orderItems) {
        this.setOrderItems(orderItems);
        return this;
    }

    public CustomerOrder addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
        return this;
    }

    public CustomerOrder removeOrderItem(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
        orderItem.setOrder(null);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public CustomerOrder customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerOrder)) {
            return false;
        }
        return getId() != null && getId().equals(((CustomerOrder) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerOrder{" +
            "id=" + getId() +
            ", orderCode='" + getOrderCode() + "'" +
            ", orderDate='" + getOrderDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", totalAmount=" + getTotalAmount() +
            "}";
    }
}
