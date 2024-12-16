package softuni.exam.instagraphlite.models.dtos.json;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PictureSeedDto {

    @NotBlank
    private String path;

    @NotNull
    @DecimalMin(value = "500")
    @DecimalMax(value = "60000")
    private Double size;
}
