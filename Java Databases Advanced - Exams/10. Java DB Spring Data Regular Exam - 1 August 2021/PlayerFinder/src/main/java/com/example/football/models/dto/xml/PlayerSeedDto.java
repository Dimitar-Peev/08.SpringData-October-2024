package com.example.football.models.dto.xml;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "player")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerSeedDto {

    @XmlElement(name = "first-name")
    @Size(min = 2)
    @NotNull
    private String firstName;

    @XmlElement(name = "last-name")
    @Size(min = 2)
    @NotNull
    private String lastName;

    @XmlElement
    @Email
    @NotNull
    private String email;

    @XmlElement(name = "birth-date")
    @NotNull
    private String birthDate;

    @XmlElement
    @NotNull
    private String position;

    @XmlElement
    private TownNameImportDto town;

    @XmlElement
    private TeamNameImportDto team;

    @XmlElement
    private StatIdImportDto stat;
}
