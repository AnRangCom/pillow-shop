package com.exe201.pillow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pillow.
 */
@Entity
@Table(name = "pillow")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pillow implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "material")
    private String material;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "base_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal basePrice;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pillow")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pillow" }, allowSetters = true)
    private Set<DefaultSize> defaultSizes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pillow id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Pillow name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Pillow description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaterial() {
        return this.material;
    }

    public Pillow material(String material) {
        this.setMaterial(material);
        return this;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public BigDecimal getBasePrice() {
        return this.basePrice;
    }

    public Pillow basePrice(BigDecimal basePrice) {
        this.setBasePrice(basePrice);
        return this;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public Set<DefaultSize> getDefaultSizes() {
        return this.defaultSizes;
    }

    public void setDefaultSizes(Set<DefaultSize> defaultSizes) {
        if (this.defaultSizes != null) {
            this.defaultSizes.forEach(i -> i.setPillow(null));
        }
        if (defaultSizes != null) {
            defaultSizes.forEach(i -> i.setPillow(this));
        }
        this.defaultSizes = defaultSizes;
    }

    public Pillow defaultSizes(Set<DefaultSize> defaultSizes) {
        this.setDefaultSizes(defaultSizes);
        return this;
    }

    public Pillow addDefaultSize(DefaultSize defaultSize) {
        this.defaultSizes.add(defaultSize);
        defaultSize.setPillow(this);
        return this;
    }

    public Pillow removeDefaultSize(DefaultSize defaultSize) {
        this.defaultSizes.remove(defaultSize);
        defaultSize.setPillow(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pillow)) {
            return false;
        }
        return getId() != null && getId().equals(((Pillow) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pillow{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", material='" + getMaterial() + "'" +
            ", basePrice=" + getBasePrice() +
            "}";
    }
}
