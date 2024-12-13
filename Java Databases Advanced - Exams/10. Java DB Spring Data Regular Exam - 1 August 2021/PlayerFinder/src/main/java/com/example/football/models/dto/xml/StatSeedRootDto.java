package com.example.football.models.dto.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "stats")
@XmlAccessorType(XmlAccessType.FIELD)
public class StatSeedRootDto {

    @XmlElement(name = "stat")
    List<StatSeedDto> stats;
}
