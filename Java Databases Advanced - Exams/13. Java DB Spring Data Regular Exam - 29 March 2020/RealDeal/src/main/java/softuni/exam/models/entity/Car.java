package softuni.exam.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String make;

    @Column(length = 20)
    private String model;

    @Column
    private Integer kilometers;

    @Column(name = "registered_on")
    private LocalDate registeredOn;

    @OneToMany(mappedBy = "car", fetch = FetchType.EAGER)
    private Set<Picture> pictures = new HashSet<>();

    @OneToMany(mappedBy = "car")
    private Set<Offer> offers = new HashSet<>();

    @Override
    public String toString() {
        return String.format("Car make - %s, model - %s\n" +
                "\tKilometers - %s\n" +
                "\tRegistered on - %s\n" +
                "\tNumber of pictures - %s\n\n",
                getMake(), getModel(), getKilometers(), getRegisteredOn(), getPictures().size());
    }
}
