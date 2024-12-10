package sofuni.exam.models.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DiscovererSeedDto {

    @Size(min = 2, max = 20)
    @NotNull
    private String firstName;

    @Size(min = 2, max = 20)
    @NotNull
    private String lastName;

    @Size(min = 5, max = 20)
    @NotNull
    private String nationality;

    @Size(min = 5, max = 20)
    private String occupation;
}
