package softuni.exam.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "forecasts")
public class Forecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DaysOfWeek dayOfWeek;

    @Min(value = -20)
    @Max(value = 60)
    @Column(nullable = false)
    private Double maxTemperature;

    @Min(value = -50)
    @Max(value = 40)
    @Column(nullable = false)
    private Double minTemperature;

    @Column(nullable = false)
    private LocalTime sunrise;

    @Column(nullable = false)
    private LocalTime sunset;

    @ManyToOne
    private City city;

    @Override
    public String toString() {

        return String.format("City: %s:\n" +
                "-min temperature: %.2f\n" +
                "--max temperature: %.2f\n" +
                "---sunrise: %s\n" +
                "----sunset: %s",
                city.getCityName(), minTemperature, maxTemperature, sunrise, sunset);
    }
}
