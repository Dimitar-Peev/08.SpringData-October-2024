package softuni.exam.models.dto.json;

import com.google.gson.annotations.Expose;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PictureSeedDto {

    @Expose
    @Size(min = 2, max = 20)
    private String name;

    @Expose
    private LocalDateTime dateAndTime;

    @Expose
    private long car;
}
