package com.example._20_exercise_xml_processing.data.dtos.imports;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Set;

@XmlRootElement(name = "customers")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerSeedRootDto {

    @XmlElement(name = "customer")
    private Set<CustomerSeedDto> customers;

    public Set<CustomerSeedDto> getCustomers() {
        return this.customers;
    }

    public void setCustomers(Set<CustomerSeedDto> customers) {
        this.customers = customers;
    }
}
