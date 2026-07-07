package com.exe201.pillow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DefaultSize.
 */
@Entity
@Table(name = "default_size")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DefaultSize implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "length", nullable = false)
    private Double length;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "width", nullable = false)
    private Double width;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "extra_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal extraPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "defaultSizes" }, allowSetters = true)
    private Pillow pillow;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DefaultSize id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public DefaultSize name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLength() {
        return this.length;
    }

    public DefaultSize length(Double length) {
        this.setLength(length);
        return this;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return this.width;
    }

    public DefaultSize width(Double width) {
        this.setWidth(width);
        return this;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public BigDecimal getExtraPrice() {
        return this.extraPrice;
    }

    public DefaultSize extraPrice(BigDecimal extraPrice) {
        this.setExtraPrice(extraPrice);
        return this;
    }

    public void setExtraPrice(BigDecimal extraPrice) {
        this.extraPrice = extraPrice;
    }

    public Pillow getPillow() {
        return this.pillow;
    }

    public void setPillow(Pillow pillow) {
        this.pillow = pillow;
    }

    public DefaultSize pillow(Pillow pillow) {
        this.setPillow(pillow);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultSize)) {
            return false;
        }
        return getId() != null && getId().equals(((DefaultSize) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DefaultSize{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", length=" + getLength() +
            ", width=" + getWidth() +
            ", extraPrice=" + getExtraPrice() +
            "}";
    }
}
