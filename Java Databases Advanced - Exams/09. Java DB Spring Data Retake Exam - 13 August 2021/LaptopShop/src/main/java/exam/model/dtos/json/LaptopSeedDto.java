package exam.model.dtos.json;

import exam.model.entities.WarrantyType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class LaptopSeedDto {

    @Size(min = 8)
    @NotNull
    private String macAddress;

    @Positive
    @NotNull
    private Double cpuSpeed;

    @Min(value = 8)
    @Max(value = 128)
    @NotNull
    private Integer ram;

    @Min(value = 128)
    @Max(value = 1024)
    @NotNull
    private Integer storage;

    @Size(min = 10)
    @NotNull
    private String description;

    @Positive
    @NotNull
    private BigDecimal price;

    @NotNull
    private WarrantyType warrantyType;

    private ShopImportNameJson shop;
}