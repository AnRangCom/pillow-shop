package com.exe201.pillow.service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class DefaultSizeDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @DecimalMin("0")
    private Double length;

    @NotNull
    @DecimalMin("0")
    private Double width;

    @NotNull
    @DecimalMin("0")
    private BigDecimal extraPrice;

    private Long pillowId;

    public DefaultSizeDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public BigDecimal getExtraPrice() {
        return extraPrice;
    }

    public void setExtraPrice(BigDecimal extraPrice) {
        this.extraPrice = extraPrice;
    }

    public Long getPillowId() {
        return pillowId;
    }

    public void setPillowId(Long pillowId) {
        this.pillowId = pillowId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultSizeDTO that = (DefaultSizeDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DefaultSizeDTO{" + "id=" + id + ", name='" + name + '\'' + ", extraPrice=" + extraPrice + ", pillowId=" + pillowId + '}';
    }
}
