package softuni.exam.models.dto.json;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class VolcanoSeedDto {

    @Expose
    @Size(min = 2, max = 30)
    private String name;

    @Expose
    @Min(value = 1)
    private int elevation;

    @Expose
    private String volcanoType;

    @Expose
    private boolean isActive;

    @Expose
    private String lastEruption;

    @Expose
    private Long country;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getElevation() {
        return this.elevation;
    }

    public void setElevation(int elevation) {
        this.elevation = elevation;
    }

    public String getVolcanoType() {
        return this.volcanoType;
    }

    public void setVolcanoType(String volcanoType) {
        this.volcanoType = volcanoType;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getLastEruption() {
        return this.lastEruption;
    }

    public void setLastEruption(String lastEruption) {
        this.lastEruption = lastEruption;
    }

    public Long getCountry() {
        return this.country;
    }

    public void setCountry(Long country) {
        this.country = country;
    }
}
