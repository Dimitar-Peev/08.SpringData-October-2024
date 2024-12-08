package softuni.exam.models.dto.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompanySeedDto {

    @JacksonXmlProperty
    @Size(min = 2, max = 40)
    private String companyName;

    @JacksonXmlProperty
    private String dateEstablished;

    @JacksonXmlProperty
    @Size(min = 2, max = 30)
    private String website;

    @JacksonXmlProperty
    private long countryId;
}
