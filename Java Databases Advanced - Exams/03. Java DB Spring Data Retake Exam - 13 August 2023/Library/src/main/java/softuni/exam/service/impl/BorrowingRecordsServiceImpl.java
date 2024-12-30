package softuni.exam.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.xml.BorrowingRecordSeedDto;
import softuni.exam.models.dto.xml.BorrowingRecordSeedRootDto;
import softuni.exam.models.entity.Book;
import softuni.exam.models.entity.BorrowingRecord;
import softuni.exam.models.entity.LibraryMember;
import softuni.exam.models.entity.enums.Genre;
import softuni.exam.repository.BookRepository;
import softuni.exam.repository.BorrowingRecordRepository;
import softuni.exam.repository.LibraryMemberRepository;
import softuni.exam.service.BorrowingRecordsService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BorrowingRecordsServiceImpl implements BorrowingRecordsService {

    private static final String FILE_PATH = "src/main/resources/files/xml/borrowing-records.xml";

    private final BorrowingRecordRepository borrowingRecordRepository;
    private final BookRepository bookRepository;
    private final LibraryMemberRepository libraryMemberRepository;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    @Override
    public boolean areImported() {
        return this.borrowingRecordRepository.count() > 0;
    }

    @Override
    public String readBorrowingRecordsFromFile() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importBorrowingRecords() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        BorrowingRecordSeedRootDto borrowingRecordSeedRootDto = this.xmlParser.convertFromFile(FILE_PATH, BorrowingRecordSeedRootDto.class);

        borrowingRecordSeedRootDto
                .getBorrowingRecordSeedDtoList()
                .stream()
                .filter(borrowingRecordSeedDto -> {
                    Optional<Book> byTitle = this.bookRepository.findByTitle(borrowingRecordSeedDto.getBook().getTitle());
                    Optional<LibraryMember> byId = this.libraryMemberRepository.findById(Long.valueOf(borrowingRecordSeedDto.getMember().getId()));

                    boolean isValid = this.validationUtil.isValid(borrowingRecordSeedDto)
                            && byTitle.isPresent()
                            && byId.isPresent();

                    sb.append(isValid ? String.format("Successfully imported borrowing record %s - %s",
                                    borrowingRecordSeedDto.getBook().getTitle(), borrowingRecordSeedDto.getBorrowDate())
                                    : "Invalid borrowing record")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(this::newBorrowingRecord)
                .forEach(borrowingRecordRepository::save);

        return sb.toString().trim();
    }

    private BorrowingRecord newBorrowingRecord(BorrowingRecordSeedDto borrowingRecordSeedDto) {
        BorrowingRecord borrowingRecord = this.modelMapper.map(borrowingRecordSeedDto, BorrowingRecord.class);

        borrowingRecord.setBook(this.bookRepository.findByTitle(borrowingRecordSeedDto.getBook().getTitle()).get());
        borrowingRecord.setMember(this.libraryMemberRepository.findById(Long.valueOf(borrowingRecordSeedDto.getMember().getId())).get());

        return borrowingRecord;
    }

    @Override
    public String exportBorrowingRecords() {
        LocalDate cutoffDate = LocalDate.parse("2021-09-10");

        List<BorrowingRecord> records = this.borrowingRecordRepository
                .findAllByBorrowDateBeforeAndOrderByBorrowDateDesc(cutoffDate, Genre.SCIENCE_FICTION);

        StringBuilder output = new StringBuilder();
        records.forEach(record -> output.append(record.toString()));
        return output.toString();
    }
}
