package hiberspring.domain.dtos.json;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeCardSeedDto {

    @NotNull
    private String number;
}
