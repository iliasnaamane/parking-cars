package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import com.mycompany.myapp.domain.enumeration.Type;

import com.mycompany.myapp.domain.enumeration.Status;

/**
 * A Slot.
 */
@Entity
@Table(name = "slot")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Slot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Min(value = 20)
    @Max(value = 20)
    @Column(name = "elec_energy")
    private Integer elecEnergy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "fixed_amount")
    private Float fixedAmount;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public Slot type(Type type) {
        this.type = type;
        return this;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getElecEnergy() {
        return elecEnergy;
    }

    public Slot elecEnergy(Integer elecEnergy) {
        this.elecEnergy = elecEnergy;
        return this;
    }

    public void setElecEnergy(Integer elecEnergy) {
        this.elecEnergy = elecEnergy;
    }

    public Status getStatus() {
        return status;
    }

    public Slot status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Float getFixedAmount() {
        return fixedAmount;
    }

    public Slot fixedAmount(Float fixedAmount) {
        this.fixedAmount = fixedAmount;
        return this;
    }

    public void setFixedAmount(Float fixedAmount) {
        this.fixedAmount = fixedAmount;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Slot)) {
            return false;
        }
        return id != null && id.equals(((Slot) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Slot{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", elecEnergy=" + getElecEnergy() +
            ", status='" + getStatus() + "'" +
            ", fixedAmount=" + getFixedAmount() +
            "}";
    }
}
