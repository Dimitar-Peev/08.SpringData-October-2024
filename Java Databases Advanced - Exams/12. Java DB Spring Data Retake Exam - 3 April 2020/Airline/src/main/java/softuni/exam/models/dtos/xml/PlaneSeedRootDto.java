package softuni.exam.models.dtos.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "planes")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlaneSeedRootDto {

    @XmlElement(name = "plane")
    private List<PlaneSeedDto> planes;

    public List<PlaneSeedDto> getPlanes() {
        return this.planes;
    }

    public void setPlanes(List<PlaneSeedDto> planes) {
        this.planes = planes;
    }
}
