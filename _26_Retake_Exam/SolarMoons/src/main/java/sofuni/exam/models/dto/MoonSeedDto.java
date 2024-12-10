package sofuni.exam.models.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MoonSeedDto {

    @Size(min = 2, max = 10)
    @NotNull
    @JacksonXmlProperty
    private String name;

    @NotNull
    @JacksonXmlProperty
    private String discovered;

    @JacksonXmlProperty(localName = "distance_from_planet")
    private Double distanceFromPlanet;

    @Positive
    @NotNull
    @JacksonXmlProperty
    private Double radius;

    @NotNull
    @JacksonXmlProperty(localName = "discoverer_id")
    private Integer discovererId;

    @NotNull
    @JacksonXmlProperty(localName = "planet_id")
    private Integer planetId;
}
