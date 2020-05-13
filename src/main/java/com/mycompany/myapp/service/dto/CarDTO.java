package com.mycompany.myapp.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.mycompany.myapp.domain.enumeration.Type;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Car} entity.
 */
public class CarDTO implements Serializable {
    
    private Long id;

    private String name;

    private Type type;

    @Min(value = 20)
    @Max(value = 20)
    private Integer elecEnergy;


    private Long slotId;
    
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

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CarDTO carDTO = (CarDTO) o;
        if (carDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), carDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CarDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", elecEnergy=" + getElecEnergy() +
            ", slotId=" + getSlotId() +
            "}";
    }
}
