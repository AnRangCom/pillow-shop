package com.exe201.pillow.domain;

import com.exe201.pillow.domain.enumeration.SizeType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OrderItem.
 */
@Entity
@Table(name = "order_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "size_type", nullable = false)
    private SizeType sizeType;

    @DecimalMin(value = "0")
    @Column(name = "custom_length")
    private Double customLength;

    @DecimalMin(value = "0")
    @Column(name = "custom_width")
    private Double customWidth;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "defaultSizes" }, allowSetters = true)
    private Pillow pillow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "pillow" }, allowSetters = true)
    private DefaultSize defaultSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orderItems", "customer" }, allowSetters = true)
    private CustomerOrder order;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public OrderItem quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public SizeType getSizeType() {
        return this.sizeType;
    }

    public OrderItem sizeType(SizeType sizeType) {
        this.setSizeType(sizeType);
        return this;
    }

    public void setSizeType(SizeType sizeType) {
        this.sizeType = sizeType;
    }

    public Double getCustomLength() {
        return this.customLength;
    }

    public OrderItem customLength(Double customLength) {
        this.setCustomLength(customLength);
        return this;
    }

    public void setCustomLength(Double customLength) {
        this.customLength = customLength;
    }

    public Double getCustomWidth() {
        return this.customWidth;
    }

    public OrderItem customWidth(Double customWidth) {
        this.setCustomWidth(customWidth);
        return this;
    }

    public void setCustomWidth(Double customWidth) {
        this.customWidth = customWidth;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public OrderItem price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Pillow getPillow() {
        return this.pillow;
    }

    public void setPillow(Pillow pillow) {
        this.pillow = pillow;
    }

    public OrderItem pillow(Pillow pillow) {
        this.setPillow(pillow);
        return this;
    }

    public DefaultSize getDefaultSize() {
        return this.defaultSize;
    }

    public void setDefaultSize(DefaultSize defaultSize) {
        this.defaultSize = defaultSize;
    }

    public OrderItem defaultSize(DefaultSize defaultSize) {
        this.setDefaultSize(defaultSize);
        return this;
    }

    public CustomerOrder getOrder() {
        return this.order;
    }

    public void setOrder(CustomerOrder customerOrder) {
        this.order = customerOrder;
    }

    public OrderItem order(CustomerOrder customerOrder) {
        this.setOrder(customerOrder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItem)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItem{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", sizeType='" + getSizeType() + "'" +
            ", customLength=" + getCustomLength() +
            ", customWidth=" + getCustomWidth() +
            ", price=" + getPrice() +
            "}";
    }
}
