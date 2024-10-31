package org.bookshop._12_exercise_spring_data_introduction.service;

import java.io.IOException;

public interface BookService {

    void seedBooks() throws IOException;

    boolean areBooksImported();

    void printAllBooksAfter2000();

    void printAllBooksFromGeorge();
}
