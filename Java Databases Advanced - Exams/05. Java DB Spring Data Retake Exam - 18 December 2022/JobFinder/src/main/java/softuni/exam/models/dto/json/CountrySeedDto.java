package softuni.exam.models.dto.json;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class CountrySeedDto {

    @NotNull(message = "Country name cannot be null")
    @Size(min = 2, max = 30, message = "Country name must be between 2 and 30 characters")
    private String name;

    @NotNull(message = "Country code cannot be null")
    @Size(min = 2, max = 19, message = "Country code must be between 2 and 19 characters")
    private String countryCode;

    @Expose
    @NotNull(message = "Currency cannot be null")
    @Size(min = 2, max = 19, message = "Currency must be between 2 and 19 characters")
    private String currency;
}
