package org.bookshop._12_exercise_spring_data_introduction.service.impls;

import org.bookshop._12_exercise_spring_data_introduction.data.entities.Author;
import org.bookshop._12_exercise_spring_data_introduction.data.repositories.AuthorRepository;
import org.bookshop._12_exercise_spring_data_introduction.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

@Service
public class AuthorServiceImpl implements AuthorService {

    private static final String AUTHORS_PATH = "src/main/resources/files/authors.txt";

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }


    @Override
    public void seedAuthors() throws IOException {

        Files.readAllLines(Path.of(AUTHORS_PATH))
                .stream()
                .filter(line -> !line.trim().isEmpty())
                .forEach(line -> {
                    String[] tokens = line.split("\\s+");
                    Author author = new Author(tokens[0], tokens[1]);
                    authorRepository.saveAndFlush(author);
                });

        System.out.printf("Count of imported Authors - %d%n", this.authorRepository.count());

    }

    @Override
    public boolean areAuthorsImported() {
        return this.authorRepository.count() > 0;
    }

    @Override
    public void printAllAuthorsWithOneBookBefore1990() {
        this.authorRepository.findAllByBooksReleaseDateBefore(LocalDate.of(1990, 1, 1))
                .forEach(author -> {
                    System.out.printf("%s %s - %d%n", author.getFirstName(), author.getLastName(), author.getBooks().size());
                });
//        this.authorRepository.findAll()
//                .stream()
//                .filter(author -> author.getBooks()
//                        .stream().anyMatch(b -> b.getReleaseDate().isBefore(LocalDate.of(1990, 1, 1))))
//                .forEach(author -> System.out.printf("%s %s%n", author.getFirstName(), author.getLastName()));
    }

    @Override
    public void printAllAuthorsWithDescCount() {
        this.authorRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getBooks().size() - a.getBooks().size())
                .forEach(author -> System.out.printf("%s %s - %d%n", author.getFirstName(), author.getLastName(), author.getBooks().size()));
    }
}
