package softuni.exam.models.dto.xml;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlRootElement(name = "member")
@XmlAccessorType(XmlAccessType.FIELD)
public class LibraryMemberImportDto {

    @XmlElement(name = "id")
    private Integer id;
}
