package softuni.exam.models.dto.json;

import com.google.gson.annotations.Expose;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CarSeedDto {

    @Expose
    @Size(min = 2, max = 20)
    private String make;

    @Expose
    @Size(min = 2, max = 20)
    private String model;

    @Expose
    @Positive
    private int kilometers;

    @Expose
    private LocalDate registeredOn;
}
