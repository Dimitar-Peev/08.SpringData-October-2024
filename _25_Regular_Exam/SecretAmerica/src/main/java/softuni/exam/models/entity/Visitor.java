package softuni.exam.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "visitors")
public class Visitor extends BaseEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "attraction_id", nullable = false)
    private Attraction attraction;

    @ManyToOne(optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @OneToOne(optional = false)
    @JoinColumn(name = "personal_data_id", nullable = false, unique = true)
    private PersonalData personalData;
}
