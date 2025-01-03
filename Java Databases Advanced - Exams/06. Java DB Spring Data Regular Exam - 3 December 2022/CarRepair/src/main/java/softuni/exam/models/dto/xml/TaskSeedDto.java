package softuni.exam.models.dto.xml;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "task")
@XmlAccessorType(XmlAccessType.FIELD)
public class TaskSeedDto {

    @NotNull
    @Positive
    @XmlElement
    private BigDecimal price;

    @NotNull
    @XmlElement
    private String date;

    @NotNull
    @XmlElement
    private CarBase car;

    @NotNull
    @XmlElement
    private MechanicBase mechanic;

    @NotNull
    @XmlElement
    private PartBase part;
}
