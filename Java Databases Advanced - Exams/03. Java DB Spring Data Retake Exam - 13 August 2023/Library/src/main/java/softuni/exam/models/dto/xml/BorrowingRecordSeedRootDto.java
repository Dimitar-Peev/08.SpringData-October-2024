package softuni.exam.models.dto.xml;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@XmlRootElement(name = "borrowing_records")
@XmlAccessorType(XmlAccessType.FIELD)
public class BorrowingRecordSeedRootDto {

    @XmlElement(name = "borrowing_record")
    private List<BorrowingRecordSeedDto> borrowingRecordSeedDtoList;
}
