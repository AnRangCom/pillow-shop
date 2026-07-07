package com.exe201.pillow.service.dto;

import com.exe201.pillow.domain.enumeration.SizeType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class OrderItemDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotNull
    private SizeType sizeType;

    @DecimalMin("0")
    private Double customLength;

    @DecimalMin("0")
    private Double customWidth;

    @DecimalMin("0")
    private BigDecimal price;

    private Long pillowId;

    private String pillowName;

    private Long defaultSizeId;

    private String defaultSizeName;

    private Long orderId;

    public OrderItemDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public SizeType getSizeType() {
        return sizeType;
    }

    public void setSizeType(SizeType sizeType) {
        this.sizeType = sizeType;
    }

    public Double getCustomLength() {
        return customLength;
    }

    public void setCustomLength(Double customLength) {
        this.customLength = customLength;
    }

    public Double getCustomWidth() {
        return customWidth;
    }

    public void setCustomWidth(Double customWidth) {
        this.customWidth = customWidth;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getPillowId() {
        return pillowId;
    }

    public void setPillowId(Long pillowId) {
        this.pillowId = pillowId;
    }

    public String getPillowName() {
        return pillowName;
    }

    public void setPillowName(String pillowName) {
        this.pillowName = pillowName;
    }

    public Long getDefaultSizeId() {
        return defaultSizeId;
    }

    public void setDefaultSizeId(Long defaultSizeId) {
        this.defaultSizeId = defaultSizeId;
    }

    public String getDefaultSizeName() {
        return defaultSizeName;
    }

    public void setDefaultSizeName(String defaultSizeName) {
        this.defaultSizeName = defaultSizeName;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemDTO that = (OrderItemDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return (
            "OrderItemDTO{" +
            "id=" +
            id +
            ", quantity=" +
            quantity +
            ", sizeType=" +
            sizeType +
            ", price=" +
            price +
            ", pillowId=" +
            pillowId +
            ", orderId=" +
            orderId +
            '}'
        );
    }
}
