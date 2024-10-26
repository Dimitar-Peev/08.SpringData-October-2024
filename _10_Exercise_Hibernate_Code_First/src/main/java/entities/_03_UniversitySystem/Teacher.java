package entities._03_UniversitySystem;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "teachers")
public class Teacher extends Person {
    @Basic
    private String email;

    @Column(name = "hourly_rate")
    private double hourlyRate;

//    ToMany
//    List<Course> courses;

    public Teacher() {
        super();
    }
}
