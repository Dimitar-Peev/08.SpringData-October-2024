package softuni.exam.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "passengers")
public class Passenger extends BaseEntity {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(unique = true, nullable = false)
    private String email;

    @ManyToOne
    private Town town;

    @OneToMany(mappedBy = "passenger", targetEntity = Ticket.class)
    private List<Ticket> tickets;
}
