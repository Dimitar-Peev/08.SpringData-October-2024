package softuni.exam.domain.dtos.json;

import com.google.gson.annotations.Expose;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import softuni.exam.domain.dtos.xml.PictureSeedDto;
import softuni.exam.domain.dtos.xml.TeamSeedDto;
import softuni.exam.domain.entities.Position;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PlayerSeedDto {

    @Expose
    @NotNull
    private String firstName;

    @Expose
    @Length(min = 3, max = 15)
    private String lastName;

    @Expose
    @Min(value = 1)
    @Max(value = 99)
    private Integer number;

    @Expose
    @DecimalMin(value = "0")
    private BigDecimal salary;

    @Expose
    @NotNull
    private Position position;

    @Expose
    @NotNull
    private PictureSeedDto picture;

    @Expose
    @NotNull
    private TeamSeedDto team;
}
