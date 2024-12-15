package exam.model.dtos.xml;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.xml.bind.annotation.*;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "shop")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShopSeedDto {

    @XmlElement
    @Length(min = 4)
    private String address;

    @XmlElement(name = "employee-count")
    @Min(value = 1)
    @Max(value = 50)
    private Integer employeeCount;

    @XmlElement
    @Min(value = 20000)
    private long income;

    @XmlElement
    @Length(min = 4)
    private String name;

    @XmlElement(name = "shop-area")
    @Min(value = 150)
    private Integer shopArea;

    @XmlElement(name = "town")
    private TownImportNameXml town;
}
