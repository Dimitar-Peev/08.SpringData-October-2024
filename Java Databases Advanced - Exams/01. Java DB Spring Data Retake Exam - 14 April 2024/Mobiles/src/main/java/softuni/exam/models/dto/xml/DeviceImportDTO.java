package softuni.exam.models.dto.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
public class DeviceImportDTO {

    @JacksonXmlProperty
    @Size(min = 2, max = 20)
    private String brand;

    @JacksonXmlProperty(localName = "device_type")
    private String deviceType;

    @JacksonXmlProperty
    @Size(min = 1, max = 20)
    private String model;

    @JacksonXmlProperty
    @Positive
    private Double price;

    @JacksonXmlProperty
    @Positive
    private Integer storage;

    @JacksonXmlProperty(localName = "sale_id")
    private Long saleId;

}