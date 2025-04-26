package softuni.exam.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pictures")
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 20)
    private String name;

    @Column(name = "date_and_time")
    private LocalDateTime dateAndTime;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToMany(mappedBy = "pictures")
    private Set<Offer> offers = new HashSet<>();
}
