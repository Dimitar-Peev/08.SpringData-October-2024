package softuni.exam.models.dtos.json;

import com.google.gson.annotations.Expose;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PassengerSeedDto {

    @Expose
    @Size(min = 2)
    private String firstName;

    @Expose
    @Size(min = 2)
    private String lastName;

    @Expose
    @Positive
    private int age;

    @Expose
    private String phoneNumber;

    @Expose
    @Email
    private String email;

    @Expose
    private String town;
}
