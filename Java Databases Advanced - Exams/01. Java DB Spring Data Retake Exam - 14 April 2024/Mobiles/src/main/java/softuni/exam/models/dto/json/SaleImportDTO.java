package softuni.exam.models.dto.json;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@NoArgsConstructor
public class SaleImportDTO {

    private boolean discounted;

    @Length(min = 7, max = 7)
    private String number;

    private String saleDate;

    private Long seller;

}
