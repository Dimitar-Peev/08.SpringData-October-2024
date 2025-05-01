package softuni.exam.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "towns")
public class Town extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String townName;

    @Column(nullable = false)
    private Integer population;

    @OneToMany(mappedBy = "town")
    private List<Agent> agents = new ArrayList<>();

    @OneToMany(mappedBy = "town")
    private List<Apartment> apartments = new ArrayList<>();
}
