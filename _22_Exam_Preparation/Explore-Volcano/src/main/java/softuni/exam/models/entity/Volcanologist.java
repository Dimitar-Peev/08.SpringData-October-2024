package softuni.exam.models.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "volcanologists")
public class Volcanologist extends BaseEntity {

    @Column(name = "first_name", unique = true, nullable = false)
    private String firstName;

    @Column(name = "last_name", unique = true, nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Double salary;

    @Column(nullable = false)
    private Integer age;

    @Column(name = "exploring_from")
    private LocalDate exploringFrom;

    @ManyToOne
    @JoinColumn(name = "exploring_volcano_id", referencedColumnName = "id")
    private Volcano exploringVolcano;

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

    public Integer getAge() {
        return this.age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDate getExploringFrom() {
        return this.exploringFrom;
    }

    public void setExploringFrom(LocalDate exploringFrom) {
        this.exploringFrom = exploringFrom;
    }

    public Volcano getExploringVolcano() {
        return this.exploringVolcano;
    }

    public void setExploringVolcano(Volcano exploringVolcano) {
        this.exploringVolcano = exploringVolcano;
    }


}
