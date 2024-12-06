package softuni.exam.models.dto.json;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@NoArgsConstructor
public class SellerImportDTO {

    @Length(min = 2, max = 30)
    private String firstName;

    @Length(min = 2, max = 30)
    private String lastName;

    @Length(min = 3, max = 6)
    private String personalNumber;

}
