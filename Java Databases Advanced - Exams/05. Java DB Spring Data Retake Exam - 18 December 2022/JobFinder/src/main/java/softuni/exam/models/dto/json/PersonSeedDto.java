package softuni.exam.models.dto.json;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PersonSeedDto {

    @NotNull(message = "Email cannot be null")
    @Pattern(regexp = "^[^@]+@[^@.]+\\.[^@.]+$", message = "Must be a valid email address")
    private String email;

    @NotNull(message = "First name cannot be null")
    @Size(min = 2, max = 30, message = "First name must be between 2 and 30 characters")
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @Size(min = 2, max = 30, message = "Last name must be between 2 and 30 characters")
    private String lastName;

    @Size(min = 2, max = 13, message = "Phone number must be between 2 and 13 characters")
    private String phone;

    private String statusType;

    @NotNull(message = "Country ID cannot be null")
    private Long country;
}
