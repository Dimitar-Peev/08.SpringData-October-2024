package com.example.productsshop.service.dtos.export;

import com.google.gson.annotations.Expose;

import java.util.Set;

public class UserSoldJsonDto {
    @Expose
    private String firstName;

    @Expose
    private String lastName;

    @Expose
    private Set<ProductSoldJsonDto> soldProducts;

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<ProductSoldJsonDto> getSoldProducts() {
        return this.soldProducts;
    }

    public void setSoldProducts(Set<ProductSoldJsonDto> soldProducts) {
        this.soldProducts = soldProducts;
    }
}
