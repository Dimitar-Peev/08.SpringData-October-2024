package softuni.exam.models.dtos.xml;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "plane")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlaneSeedDto {

    @XmlElement(name = "register-number")
    @Size(min = 5)
    private String registerNumber;

    @XmlElement
    @Positive
    private int capacity;

    @XmlElement
    @Size(min = 2)
    private String airline;

    public @Size(min = 5) String getRegisterNumber() {
        return this.registerNumber;
    }

    public void setRegisterNumber(@Size(min = 5) String registerNumber) {
        this.registerNumber = registerNumber;
    }

    @Positive
    public int getCapacity() {
        return this.capacity;
    }

    public void setCapacity(@Positive int capacity) {
        this.capacity = capacity;
    }

    public @Size(min = 2) String getAirline() {
        return this.airline;
    }

    public void setAirline(@Size(min = 2) String airline) {
        this.airline = airline;
    }
}
