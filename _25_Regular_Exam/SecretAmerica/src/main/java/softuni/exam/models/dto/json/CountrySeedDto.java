package softuni.exam.models.dto.json;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CountrySeedDto {

    @NotBlank
    @Size(min = 3, max = 40)
    private String name;

    @Positive
    private Double area;
}
