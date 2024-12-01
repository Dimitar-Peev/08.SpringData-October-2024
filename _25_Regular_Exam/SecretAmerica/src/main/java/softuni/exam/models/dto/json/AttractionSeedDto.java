package softuni.exam.models.dto.json;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AttractionSeedDto {

    @Size(min = 5, max = 40)
    @NotNull
    private String name;

    @Size(min = 10, max = 100)
    @NotNull
    private String description;

    @Size(min = 3, max = 30)
    @NotNull
    private String type;

    @Min(0)
    @NotNull
    private Integer elevation;

    @NotNull
    private Long country;
}
