package softuni.exam.instagraphlite.models.dtos.json;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSeedDto {

    @NotBlank
    @Size(min = 2, max = 18)
    private String username;

    @NotNull
    @Size(min = 4)
    private String password;

    @NotBlank
    private String profilePicture;
}
