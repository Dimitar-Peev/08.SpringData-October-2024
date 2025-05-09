package softuni.exam.models.dto.json;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.Size;
import java.io.Serializable;

public class ConstellationSeedDto implements Serializable {

    @Expose
    @Size(min = 3, max = 20)
    private String name;

    @Size(min = 5)
    @Expose
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
