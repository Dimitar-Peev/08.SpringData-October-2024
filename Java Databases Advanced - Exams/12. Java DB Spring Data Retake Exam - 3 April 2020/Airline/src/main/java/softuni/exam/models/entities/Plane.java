package softuni.exam.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "planes")
public class Plane extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String registerNumber;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private String airline;

    @OneToMany(mappedBy = "plane")
    private List<Ticket> tickets;
}
