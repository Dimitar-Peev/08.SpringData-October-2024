package sofuni.exam.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sofuni.exam.models.enums.Type;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "planets")
public class Planet extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int diameter;

    @Column(nullable = false)
    private Long distanceFromSun;

    @Column(nullable = false)
    private Double orbitalPeriod;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @OneToMany(mappedBy = "planet")
    private List<Moon> moons = new ArrayList<>();
}
