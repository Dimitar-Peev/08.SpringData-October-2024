package hiberspring.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "employees")
public class Employee extends BaseEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String position;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private Branch branch;

    @OneToOne(cascade = {CascadeType.MERGE})
    private EmployeeCard card;

    @Override
    public String toString() {
        return String.format("""
                        Name: %s %s
                        Position: %s
                        Card Number: %s
                        -------------------------""",
                this.getFirstName(), this.getLastName(), this.getPosition(), this.getCard().getNumber());
    }
}
