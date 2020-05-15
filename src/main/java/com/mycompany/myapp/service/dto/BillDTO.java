package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Car} entity.
 */
public class BillDTO implements Serializable {
    
    private Long id;
    
    private Integer numberHours;
    
    private Integer hourPrice;
    
    private Float finalPrice;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumberHours() {
        return numberHours;
    }

    public void setNumberHours(Integer numberHours) {
        this.numberHours = numberHours;
    }
    
    public Integer getHourPrice() {
        return hourPrice;
    }

    public void setHourPrice(Integer hourPrice) {
        this.hourPrice = hourPrice;
    }

    public Float getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Float finalPrice) {
        this.finalPrice = finalPrice;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BillDTO billDTO = (BillDTO) o;
        if (billDTO.getId() == null || getId()== null) {
            return false;
        }
        return Objects.equals(getId(), billDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BillDTO{" +
            "id=" +  getId() +
            ", numberhours='" + getNumberHours()+ "'" +
             ", finalPrice='" + getFinalPrice()+ "'" +
            "}";
    }
}
