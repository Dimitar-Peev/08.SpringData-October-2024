package com.example.productsshop.service.dtos;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

public class CategorySeedJsonDto {
    @Expose
    @Length(min = 3, max = 15)
    private String name;

    public CategorySeedJsonDto() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
