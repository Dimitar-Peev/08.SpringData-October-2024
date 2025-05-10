package softuni.exam.models.dto.xml;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.time.DayOfWeek;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "forecast")
@XmlAccessorType(XmlAccessType.FIELD)
public class ForecastImportDto {

    @NotNull
    @XmlElement(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @DecimalMax(value = "60")
    @DecimalMin(value = "-20")
    @NotNull
    @XmlElement(name = "max_temperature")
    private Double maxTemperature;

    @DecimalMax(value = "40")
    @DecimalMin(value = "-50")
    @NotNull
    @XmlElement(name = "min_temperature")
    private Double minTemperature;

    @NotNull
    @XmlElement
    private String sunrise;

    @NotNull
    @XmlElement
    private String sunset;

    @NotNull
    @XmlElement
    private Long city;
}
