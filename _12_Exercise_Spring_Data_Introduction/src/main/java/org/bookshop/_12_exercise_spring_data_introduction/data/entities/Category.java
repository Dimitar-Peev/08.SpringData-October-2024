package org.bookshop._12_exercise_spring_data_introduction.data.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

//    // bidirectional
//    @ManyToMany(mappedBy = "categories")
//    private Set<Book> books;
//
//    public Set<Book> getBooks() {
//        return this.books;
//    }
//
//    public void setBooks(Set<Book> books) {
//        this.books = books;
//    }

    public Category() {
//        this.books = new HashSet<>();
    }

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
