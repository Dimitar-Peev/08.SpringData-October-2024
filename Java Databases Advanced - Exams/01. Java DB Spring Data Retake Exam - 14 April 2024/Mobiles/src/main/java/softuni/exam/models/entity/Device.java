package softuni.exam.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "devices")
public class Device extends BaseEntity {

    @Column(nullable = false)
    private String brand;

    @Column
    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    @Column(unique = true, nullable = false)
    private String model;

    @Column
    @Positive
    private Double price;

    @Column
    @Positive
    private int storage;

    @ManyToOne
    private Sale sale;

    @Override
    public String toString() {
        Locale locale = Locale.US;
        DecimalFormat df = new DecimalFormat("####.00", DecimalFormatSymbols.getInstance(locale));

        return String.format("Device brand: %s\n   *Model: %s\n   **Storage: %d\n   ***Price: %s",
                this.getBrand(), this.getModel(), this.getStorage(), df.format(this.getPrice()));
    }
}
