package com.example.productsshop.service.dtos.export;

import com.google.gson.annotations.Expose;

import java.util.Set;

public class UserAndSoldProductDto {
    @Expose
    private int usersCount;

    @Expose
    private Set<FullUserSoldJsonDto> users;

    public int getUsersCount() {
        return this.usersCount;
    }

    public void setUsersCount(int usersCount) {
        this.usersCount = usersCount;
    }

    public Set<FullUserSoldJsonDto> getUsers() {
        return this.users;
    }

    public void setUsers(Set<FullUserSoldJsonDto> users) {
        this.users = users;
    }
}
