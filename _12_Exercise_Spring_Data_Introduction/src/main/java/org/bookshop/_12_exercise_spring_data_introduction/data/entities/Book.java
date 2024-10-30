package org.bookshop._12_exercise_spring_data_introduction.data.entities;

import jakarta.persistence.*;
import org.bookshop._12_exercise_spring_data_introduction.data.entities.enums.AgeRestriction;
import org.bookshop._12_exercise_spring_data_introduction.data.entities.enums.EditionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book extends BaseEntity {

    @Column(name = "age_restriction", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private AgeRestriction ageRestriction;

    @Column
    private int copies;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private EditionType editionType;

    @Column
    private BigDecimal price;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(length = 50, nullable = false)
    private String title;

    @ManyToMany
    @JoinTable(name = "books_categories",
            joinColumns = @JoinColumn(name = "book_id",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    private Set<Category> categories;

    @ManyToOne
    @JoinColumn(name = "author_id",referencedColumnName = "id")
    private Author author;

    public Book() {
        this.categories = new HashSet<>();
    }

    public Book(AgeRestriction ageRestriction, int copies, EditionType editionType, BigDecimal price, LocalDate releaseDate, String title) {
        this.ageRestriction = ageRestriction;
        this.copies = copies;
        this.editionType = editionType;
        this.price = price;
        this.releaseDate = releaseDate;
        this.title = title;
    }

    public Author getAuthor() {
        return this.author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public AgeRestriction getAgeRestriction() {
        return this.ageRestriction;
    }

    public void setAgeRestriction(AgeRestriction ageRestriction) {
        this.ageRestriction = ageRestriction;
    }

    public int getCopies() {
        return this.copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EditionType getEditionType() {
        return this.editionType;
    }

    public void setEditionType(EditionType editionType) {
        this.editionType = editionType;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getReleaseDate() {
        return this.releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Category> getCategories() {
        return this.categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}
