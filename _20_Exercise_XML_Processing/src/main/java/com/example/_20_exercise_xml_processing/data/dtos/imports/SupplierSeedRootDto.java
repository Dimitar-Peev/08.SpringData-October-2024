package com.example._20_exercise_xml_processing.data.dtos.imports;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Set;

@XmlRootElement(name = "suppliers")
@XmlAccessorType(XmlAccessType.FIELD)
public class SupplierSeedRootDto {

    @XmlElement(name = "supplier")
    public Set<SupplierSeedDto> suppliers;

    public Set<SupplierSeedDto> getSuppliers() {
        return this.suppliers;
    }

    public void setSuppliers(Set<SupplierSeedDto> suppliers) {
        this.suppliers = suppliers;
    }
}
