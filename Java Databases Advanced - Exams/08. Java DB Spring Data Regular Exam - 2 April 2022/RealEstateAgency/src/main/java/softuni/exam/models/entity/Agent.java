package softuni.exam.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "agents")
public class Agent extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @ManyToOne
    private Town town;

    @OneToMany(mappedBy = "agent")
    private List<Offer> offers = new ArrayList<>();
}
