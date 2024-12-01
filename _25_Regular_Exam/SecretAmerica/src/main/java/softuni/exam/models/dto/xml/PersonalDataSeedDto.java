package softuni.exam.models.dto.xml;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
@XmlRootElement(name = "personal_data")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonalDataSeedDto {

    @XmlElement
    @Min(1)
    @Max(100)
    private int age;

    @XmlElement
    @Size(min = 1, max = 1)
    private String gender;

    @XmlElement(name = "birth_date")
    private String birthDate;

    @XmlElement(name = "card_number")
    @NotNull
    @Size(min = 9, max = 9)
    private String cardNumber;
}
