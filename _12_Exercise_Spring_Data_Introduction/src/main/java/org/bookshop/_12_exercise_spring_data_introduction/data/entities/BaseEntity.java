package org.bookshop._12_exercise_spring_data_introduction.data.entities;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    protected BaseEntity() {}

    public Long getId() {
        return this.id;
    }
}
