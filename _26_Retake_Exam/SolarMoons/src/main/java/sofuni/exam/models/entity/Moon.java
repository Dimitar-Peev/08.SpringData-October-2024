package sofuni.exam.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "moons")
public class Moon extends BaseEntity {

    @Column(nullable = false)
    private LocalDate discovered;

    @Column
    private int distanceFromPlanet;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Double radius;

    @ManyToOne(optional = false)
    @JoinColumn(name = "planet_id", nullable = false)
    private Planet planet;

    @ManyToOne(optional = false)
    @JoinColumn(name = "discoverer_id", nullable = false)
    private Discoverer discoverer;

    @Override
    public String toString() {
        Locale locale = Locale.US;
        DecimalFormat df = new DecimalFormat("####.00", DecimalFormatSymbols.getInstance(locale));
        return String.format("***Moon %s is a natural satellite of %s and has a radius of %s km.%n" +
                "****Discovered by %s %s%n",
                this.name, this.planet.getName(), df.format(this.radius),
                this.discoverer.getFirstName(), this.discoverer.getLastName());
    }
}
