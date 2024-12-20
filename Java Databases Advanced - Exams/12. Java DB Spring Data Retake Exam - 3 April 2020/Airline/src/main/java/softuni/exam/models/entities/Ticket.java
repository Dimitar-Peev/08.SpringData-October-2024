package softuni.exam.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket extends BaseEntity {

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(columnDefinition = "DECIMAL(19,2)")
    private BigDecimal price;

    @Column(name = "take_off")
    private LocalDateTime takeoff;

    @ManyToOne
    private Plane plane;

    @ManyToOne
    private Passenger passenger;

    @ManyToOne
    @JoinColumn(name = "from_town_id")
    private Town fromTown;

    @ManyToOne
    @JoinColumn(name = "to_town_id")
    private Town toTown;
}
