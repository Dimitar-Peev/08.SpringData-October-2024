
package softuni.exam.models.dto.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JobSeedDto {

    @JacksonXmlProperty
    @NotNull
    @Size(min = 2, max = 40, message = "Accepts char sequence (between 2 to 40 inclusive)")
    private String jobTitle;

    @JacksonXmlProperty
    @NotNull
    @DecimalMin(value = "10.00", message = "Accepts number values that are more than or equal to 10.00")
    private Double hoursAWeek;

    @JacksonXmlProperty
    @NotNull
    @DecimalMin(value = "300.00", message = "Accepts number values that are more than or equal to 300.00")
    private Double salary;

    @JacksonXmlProperty
    @NotNull
    @Size(min = 5)
    private String description;

    @JacksonXmlProperty
    private long companyId;
}
