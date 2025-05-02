package softuni.exam.models.dto.json;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class TownSeedDto {

    @Size(min = 2)
    private String townName;

    @Positive
    private int population;
}
