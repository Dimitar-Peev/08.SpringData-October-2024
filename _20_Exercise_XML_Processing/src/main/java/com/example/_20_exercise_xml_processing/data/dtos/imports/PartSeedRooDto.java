package com.example._20_exercise_xml_processing.data.dtos.imports;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Set;

@XmlRootElement(name = "parts")
@XmlAccessorType(XmlAccessType.FIELD)
public class PartSeedRooDto {

    @XmlElement(name = "part")
    private Set<PartSeedDto> parts;

    public Set<PartSeedDto> getParts() {
        return this.parts;
    }

    public void setParts(Set<PartSeedDto> parts) {
        this.parts = parts;
    }
}
