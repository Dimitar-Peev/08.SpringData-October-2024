package softuni.exam.models.dto.json;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class PartSeedDto {

    @NotNull
    @Size(min = 2, max = 19)
    private String partName;

    @NotNull
    @DecimalMin(value = "10")
    @DecimalMax(value = "2000")
    private Double price;

    @NotNull
    @Positive
    private String quantity;
}
