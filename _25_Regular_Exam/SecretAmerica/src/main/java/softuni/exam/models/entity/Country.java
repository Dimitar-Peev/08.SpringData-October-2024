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
@Table(name = "countries")
public class Country extends BaseEntity {

    @Column
    private Double area;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "country")
    private Set<Attraction> attractions;
}
