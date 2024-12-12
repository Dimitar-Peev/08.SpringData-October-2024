package softuni.exam.models.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Getter
@Setter
public class TaskExportDto {
    private Long id;
    private BigDecimal price;
    private MechanicExportDto mechanic;
    private CarExportDto car;

    @Override
    public String toString() {
        Locale locale = Locale.US;
        DecimalFormat df = new DecimalFormat("####.00", DecimalFormatSymbols.getInstance(locale));

        String FORMAT = """
                Car %s %s with %dkm
                -Mechanic: %s %s - task №%d:
                 --Engine: %s
                ---Price: %s$
                """;

        return String.format(FORMAT,
                this.car.getCarMake(),
                this.car.getCarModel(),
                this.car.getKilometers(),
                this.mechanic.getFirstName(),
                this.mechanic.getLastName(),
                this.id,
                this.car.getEngine(),
                df.format(this.price)
        );

    }
}
