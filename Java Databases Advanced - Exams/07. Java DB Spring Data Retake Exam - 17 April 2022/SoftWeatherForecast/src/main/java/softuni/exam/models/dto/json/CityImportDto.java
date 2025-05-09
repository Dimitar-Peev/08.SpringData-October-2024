package softuni.exam.models.dto.json;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class CityImportDto {

    @Size(min = 2, max = 60)
    private String cityName;

    @Size(min = 2)
    private String description;

    @Min(value = 500)
    private String population;

    private Long country;
}
