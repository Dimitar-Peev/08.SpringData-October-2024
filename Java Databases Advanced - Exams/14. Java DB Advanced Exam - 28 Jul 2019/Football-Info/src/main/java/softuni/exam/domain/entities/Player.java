package softuni.exam.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "players")
public class Player extends BaseEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Integer number;

    @Column(nullable = false)
    private BigDecimal salary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position position;

    // One Player may have only one Picture, and one Picture may have many Players.
    @ManyToOne
    private Picture picture;

    // One Team may have many Players, and one Player may be appointed to only one Team.
    @ManyToOne(cascade = CascadeType.ALL)
    private Team team;
}
