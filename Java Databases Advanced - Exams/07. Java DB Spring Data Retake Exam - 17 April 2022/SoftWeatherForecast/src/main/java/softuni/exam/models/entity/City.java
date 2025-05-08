package softuni.exam.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 60)
    @Column(unique = true, nullable = false)
    private String cityName;

    @Size(min = 2)
    @Column(columnDefinition = "TEXT")
    private String description;

    @Min(value = 500)
    @Column(nullable = false)
    private Integer population;

    @ManyToOne
    private Country country;
}
