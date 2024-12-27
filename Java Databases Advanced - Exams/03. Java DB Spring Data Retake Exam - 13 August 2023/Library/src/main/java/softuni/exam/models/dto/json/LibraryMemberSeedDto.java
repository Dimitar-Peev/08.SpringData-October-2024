package softuni.exam.models.dto.json;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LibraryMemberSeedDto {

    @Expose
    @Length(min = 2, max = 40)
    private String address;

    @Expose
    @NotNull
    @Length(min = 2, max = 30)
    private String firstName;

    @Expose
    @Length(min = 2, max = 30)
    private String lastName;

    @Expose
    @Length(min = 2, max = 20)
    private String phoneNumber;
}
