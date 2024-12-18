package softuni.exam.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "teams")
public class Team extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    // One Team may have only one Picture, and one Picture may have many Teams.
    @ManyToOne(cascade = CascadeType.ALL)
    private Picture picture;
}
