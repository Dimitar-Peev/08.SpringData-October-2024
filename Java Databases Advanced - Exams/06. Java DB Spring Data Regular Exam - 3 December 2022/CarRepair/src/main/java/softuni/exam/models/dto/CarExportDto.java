package softuni.exam.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarExportDto {
    private String carMake;
    private String carModel;
    private Integer kilometers;
    private Double engine;
}
