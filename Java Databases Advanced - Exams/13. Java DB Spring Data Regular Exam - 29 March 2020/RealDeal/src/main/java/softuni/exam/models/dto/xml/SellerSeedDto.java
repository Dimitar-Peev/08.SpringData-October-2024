package softuni.exam.models.dto.xml;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "seller")
@XmlAccessorType(XmlAccessType.FIELD)
public class SellerSeedDto {

    @XmlElement(name = "first-name")
    @Size(min = 2, max = 20)
    private String firstName;

    @XmlElement(name = "last-name")
    @Size(min = 2, max = 20)
    private String lastName;

    @XmlElement
    @Pattern(regexp = "^(\\w+@\\w+)(.\\w+){2,}$")
    private String email;

    @XmlElement
    @NotNull
    private String rating;

    @XmlElement
    @NotBlank
    private String town;
}
