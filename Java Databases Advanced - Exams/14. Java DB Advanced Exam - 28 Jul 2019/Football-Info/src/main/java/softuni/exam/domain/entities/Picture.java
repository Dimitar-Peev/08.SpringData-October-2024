package softuni.exam.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pictures")
public class Picture extends BaseEntity {

    @Column(name = "url", nullable = false)
    private String url;

    // One To Many - bidirectional, must be collection ! Set OR List
//    @OneToMany(mappedBy = "picture", targetEntity = Team.class)
//    private Set<Team> teamSet;
}
