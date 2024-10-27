package entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity // Declares the class as an entity or a table
@Table(name = "students") // Declares table name
public class Student {
    @Id // PRIMARY KEY
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    @Column(name = "id")
    private long id;

    @Column(length = 50) // VARCHAR(50)
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Transient // Specifies the property that is not persistent, i.e., the value is never stored in the database
    private String school;

    @Basic // Specifies non-constraint fields explicitly
    private int age;

    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Student{id=%d, name='%s'}", id, name);
    }
}