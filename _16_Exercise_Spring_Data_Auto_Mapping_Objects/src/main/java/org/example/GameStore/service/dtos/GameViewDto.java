package org.example.GameStore.service.dtos;

import java.math.BigDecimal;

public class GameViewDto {
    private String title;
    private BigDecimal price;

    public GameViewDto() {
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

    @Override
    public String toString() {
        return String.format("%s %.2f", this.title, this.price);
    }
}
