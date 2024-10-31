package org.bookshop._12_exercise_spring_data_introduction.data.repositories;

import org.bookshop._12_exercise_spring_data_introduction.data.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Set;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Set<Author> findAllByBooksReleaseDateBefore(LocalDate localDate);
    Set<Author> findAllByOrderByBooksDesc();
}
