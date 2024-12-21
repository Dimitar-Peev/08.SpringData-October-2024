package softuni.exam.models.dtos.xml;

import jakarta.validation.constraints.Positive;
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
@XmlRootElement(name = "plane")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlaneSeedDto {

    @XmlElement(name = "register-number")
    @Size(min = 5)
    private String registerNumber;

    @XmlElement
    @Positive
    private int capacity;

    @XmlElement
    @Size(min = 2)
    private String airline;
}
