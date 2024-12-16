package softuni.exam.instagraphlite.models.dtos.xml;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PostSeedDto {

    @XmlElement(name = "caption")
    @Size(min = 21)
    @NotNull
    private String caption;

    @XmlElement(name = "user")
    @NotNull
    private UserByUsernameDto user;

    @XmlElement(name = "picture")
    @NotNull
    private PictureByPathDto picture;
}