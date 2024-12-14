package hiberspring.domain.dtos.xml;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "product")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductSeedDto {

    @XmlAttribute
    @NotNull
    private String name;

    @XmlAttribute
    @Min(value = 0)
    @NotNull
    private Integer clients;

    @XmlElement
    @NotNull
    private String branch;
}


