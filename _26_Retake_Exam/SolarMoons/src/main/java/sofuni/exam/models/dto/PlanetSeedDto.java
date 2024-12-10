package sofuni.exam.models.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlanetSeedDto {

    @Size(min = 3, max = 20)
    @NotNull
    private String name;

    @Positive
    @NotNull
    private Double diameter;

    @Positive
    @NotNull
    private Double distanceFromSun;

    @Positive
    @NotNull
    private Double orbitalPeriod;

    @NotNull
    private String type;
}
