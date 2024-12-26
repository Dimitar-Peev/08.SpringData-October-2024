package softuni.exam.service.impl;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.json.BookSeedDto;
import softuni.exam.models.entity.Book;
import softuni.exam.repository.BookRepository;
import softuni.exam.service.BookService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private static final String FILE_PATH = "src/main/resources/files/json/books.json";

    private final BookRepository bookRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    @Override
    public boolean areImported() {
        return this.bookRepository.count() > 0;
    }

    @Override
    public String readBooksFromFile() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importBooks() throws IOException {
        StringBuilder output = new StringBuilder();

        BookSeedDto[] bookSeedDtos = this.gson.fromJson(readBooksFromFile(), BookSeedDto[].class);

        Arrays.stream(bookSeedDtos)
                .filter(bookSeedDto -> {
                    Optional<Book> byTitle = this.bookRepository.findByTitle(bookSeedDto.getTitle());

                    boolean isValid = this.validationUtil.isValid(bookSeedDto) && byTitle.isEmpty();

                    output.append(isValid ? String.format("Successfully imported book %s - %s",
                                    bookSeedDto.getAuthor(), bookSeedDto.getTitle())
                                    : "Invalid book")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(bookSeedDto -> this.modelMapper.map(bookSeedDto, Book.class))
                .forEach(bookRepository::save);

        return output.toString().trim();
    }
}
