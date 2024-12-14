package hiberspring.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "branches")
public class Branch extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Town town;

    @OneToMany(mappedBy = "branch")
    private Set<Product> products;
}
