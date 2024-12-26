package softuni.exam.models.dto.json;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
public class BookSeedDto {

    @Expose
    @Length(min = 3, max = 40)
    private String author;

    @Expose
    private boolean available;

    @Expose
    @Length(min = 5)
    private String description;

    @Expose
    private String genre;

    @Expose
    @Length(min = 3, max = 40)
    private String title;

    @Expose
    @Positive
    private Double rating;
}
