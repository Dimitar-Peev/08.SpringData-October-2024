package org.example.GameStore.service.dtos;

import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GameEditDto {
    private Integer id;
    @Length(min = 3, max = 100, message = "Title must have a length between 3 and 100 symbols (inclusively).")
    private String title;
    @Positive(message = "Price must be a positive number.")
    private BigDecimal price;
    @Positive(message = "Size must be a positive number.")
    private Double size;
    @Length(min = 11, max = 11,message = "Trailer must be exactly 11 characters.")
    private String trailer;

    private String imageThumbnail;
    @Length(min = 20)
    private String description;
    private LocalDate releaseDate;

    public GameEditDto() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Double getSize() {
        return this.size;
    }

    public String getTrailer() {
        return this.trailer;
    }

    public String getImageThumbnail() {
        return this.imageThumbnail;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDate getReleaseDate() {
        return this.releaseDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public void setImageThumbnail(String imageThumbnail) {
        this.imageThumbnail = imageThumbnail;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
}
