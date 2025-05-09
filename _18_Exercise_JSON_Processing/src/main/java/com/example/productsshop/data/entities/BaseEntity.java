package com.example.productsshop.data.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    protected BaseEntity() {}

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
