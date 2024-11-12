package com.example.productsshop.service.dtos.export;

import com.google.gson.annotations.Expose;

import java.util.Set;

public class SoldProductsDto {
    @Expose
    private int count;

    @Expose
    private Set<ProductInfoDto> products;

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Set<ProductInfoDto> getProducts() {
        return this.products;
    }

    public void setProducts(Set<ProductInfoDto> products) {
        this.products = products;
    }
}
