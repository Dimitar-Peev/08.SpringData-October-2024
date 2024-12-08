package softuni.exam.models.dto.xml;

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
@JacksonXmlRootElement(localName = "jobs")
public class JobSeedRootDto {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "job")
    private List<JobSeedDto> jobs;
}
