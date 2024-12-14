package hiberspring.domain.dtos.xml;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "employee")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmployeeSeedDto {

    @XmlAttribute(name = "first-name")
    @NotNull
    private String firstName;

    @XmlAttribute(name = "last-name")
    @NotNull
    private String lastName;

    @XmlAttribute
    @NotNull
    private String position;

    @XmlElement
    @NotNull
    private String card;

    @XmlElement
    @NotNull
    private String branch;
}


