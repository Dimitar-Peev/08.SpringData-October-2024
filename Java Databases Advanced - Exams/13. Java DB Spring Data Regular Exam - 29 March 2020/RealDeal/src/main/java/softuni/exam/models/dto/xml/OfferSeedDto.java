package softuni.exam.models.dto.xml;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Data;
import softuni.exam.config.LocalDateTimeAdapter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@XmlRootElement(name = "offer")
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferSeedDto {

    @XmlElement(name = "description")
    @Size(min = 5)
    private String description;

    @XmlElement(name = "price")
    @DecimalMin(value = "0")
    private BigDecimal price;

    @XmlElement(name = "added-on")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime addedOn;

    @XmlElement(name = "has-gold-status")
    private boolean hasGoldStatus;

    @XmlElement(name = "car")
    private CarImportDto car;

    @XmlElement(name = "seller")
    private SellerImportDto seller;
}
