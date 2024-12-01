package softuni.exam.models.dto.xml;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "visitor")
@XmlAccessorType(XmlAccessType.FIELD)
public class VisitorSeedDto {

    @XmlElement(name = "first_name")
    @Size(min = 2, max = 20)
    @NotNull
    private String firstName;

    @XmlElement(name = "last_name")
    @Size(min = 2, max = 20)
    @NotNull
    private String lastName;

    @XmlElement(name = "attraction_id")
    @NotNull
    private Integer attractionId;

    @XmlElement(name = "country_id")
    @NotNull
    private Long countryId;

    @XmlElement(name = "personal_data_id")
    @NotNull
    private Long personalDataId;


}


