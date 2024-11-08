package org.example.GameStore.service.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GameDetailDto {
    private String title;
    private BigDecimal price;
    private String description;
    private LocalDate releaseDate;

    public GameDetailDto() {
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return this.releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        if (this.title == null) {
            return "No such game";
        }

        return String.format("""
                Title: %s
                Price: %.2f
                Description: %s
                Release date: %s""", title, price, description, releaseDate);
    }
}
