package com.example.productsshop.service.dtos;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

public class UserSeedJsonDto {
    @Expose
    private String firstName;

    @Expose
    @Length(min = 3)
    private String lastName;

    @Expose
    private Integer age;

    public UserSeedJsonDto() {
    }

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

    public Integer getAge() {
        return this.age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
