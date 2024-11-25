package com.example._20_exercise_xml_processing.data.dtos.imports;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Set;

@XmlRootElement(name = "cars")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarSeedRootDto {

    @XmlElement(name = "car")
    private Set<CarSeedDto> cars;

    public Set<CarSeedDto> getCars() {
        return this.cars;
    }

    public void setCars(Set<CarSeedDto> cars) {
        this.cars = cars;
    }
}
