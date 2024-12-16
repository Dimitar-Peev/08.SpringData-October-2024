package softuni.exam.instagraphlite.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExportPictureDto {

    private Double size;

    private String path;
}