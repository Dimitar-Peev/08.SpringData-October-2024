package exam.model.dtos.xml;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "town")
@XmlAccessorType(XmlAccessType.FIELD)
public class TownSeedDto {

    @XmlElement
    @NotNull
    @Length(min = 2)
    private String name;

    @XmlElement
    @NotNull
    @Positive
    private Integer population;

    @XmlElement(name = "travel-guide")
    @NotNull
    @Length(min = 10)
    private String travelGuide;

}
