package com.mycompany.myapp.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.mycompany.myapp.domain.enumeration.Type;
import com.mycompany.myapp.domain.enumeration.Status;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Slot} entity.
 */
public class SlotDTO implements Serializable {
    
    private Long id;

    private Type type;

    @Min(value = 20)
    @Max(value = 20)
    private Integer elecEnergy;

    private Status status;

    private Float fixedAmount;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getElecEnergy() {
        return elecEnergy;
    }

    public void setElecEnergy(Integer elecEnergy) {
        this.elecEnergy = elecEnergy;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Float getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(Float fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SlotDTO slotDTO = (SlotDTO) o;
        if (slotDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), slotDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SlotDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", elecEnergy=" + getElecEnergy() +
            ", status='" + getStatus() + "'" +
            ", fixedAmount=" + getFixedAmount() +
            "}";
    }
}
