package softuni.exam.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "attractions")
public class Attraction extends BaseEntity {

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer elevation;

    @Column(nullable = false,unique = true)
    private String name;

    @Column(nullable = false)
    private String type;

    @ManyToOne(optional = false)
    private Country country;

    @OneToMany(mappedBy = "attraction")
    private Set<Visitor> visitors;
}
