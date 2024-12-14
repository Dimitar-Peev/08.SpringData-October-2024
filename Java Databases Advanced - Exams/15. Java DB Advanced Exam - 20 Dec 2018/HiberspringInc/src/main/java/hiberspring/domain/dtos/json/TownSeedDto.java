package hiberspring.domain.dtos.json;

import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TownSeedDto {

    @NotNull
    @SerializedName(value = "name")
    private String townName;

    @NotNull
    private Integer population;
}
