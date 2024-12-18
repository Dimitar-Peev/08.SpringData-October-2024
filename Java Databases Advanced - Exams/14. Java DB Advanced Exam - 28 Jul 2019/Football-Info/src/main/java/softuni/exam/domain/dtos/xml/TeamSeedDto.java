package softuni.exam.domain.dtos.xml;

import com.google.gson.annotations.Expose;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "team")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamSeedDto {

    @XmlElement
    @Length(min = 3, max = 20, message = "Name must be between 3 and 20 characters.")
    @Expose
    private String name;

    @XmlElement(name = "picture")
    @NotNull
    @Expose
    private PictureSeedDto picture;
}
