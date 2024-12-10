package sofuni.exam.models.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "moons")
public class MoonSeedRootDto {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "moon")
    private List<MoonSeedDto> moons;
}
