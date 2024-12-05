package softuni.exam.models.dto.xml;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "volcanologist")
@XmlAccessorType(XmlAccessType.FIELD)
public class VolcanologistSeedDto implements Serializable {

    @XmlElement(name = "first_name")
    @Length(min = 2, max = 30)
    private String firstName;

    @XmlElement(name = "last_name")
    @Length(min = 2, max = 30)
    private String lastName;

    @XmlElement(name = "salary")
    private Double salary;

    @XmlElement(name = "age")
    @Min(value = 18)
    @Max(value = 80)
    private int age;

    @XmlElement(name = "exploring_from")
    private String exploringFrom;

    @XmlElement(name = "exploring_volcano_id")
    private Long exploringVolcano;


    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Double getSalary() {
        return this.salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getExploringFrom() {
        return this.exploringFrom;
    }

    public void setExploringFrom(String exploringFrom) {
        this.exploringFrom = exploringFrom;
    }

    public Long getExploringVolcano() {
        return this.exploringVolcano;
    }

    public void setExploringVolcano(Long exploringVolcano) {
        this.exploringVolcano = exploringVolcano;
    }
}
