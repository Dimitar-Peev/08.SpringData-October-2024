package exam.model.dtos.json;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerSeedDto {

    @Size(min = 2)
    @NotNull
    private String firstName;

    @Size(min = 2)
    @NotNull
    private String lastName;

    @Email
    @NotNull
    private String email;

    @NotNull
    private String registeredOn;

    private TownImportNameJson town;
}