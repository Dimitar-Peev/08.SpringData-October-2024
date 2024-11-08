package org.example.GameStore.service;

import org.example.GameStore.data.entities.User;
import org.example.GameStore.service.dtos.UserCreateDto;
import org.example.GameStore.service.dtos.UserLoginDto;

public interface UserService {
    String registerUser(UserCreateDto userCreateDto);

    String loginUser(UserLoginDto userLoginDto);

    User getUser();

    boolean isLoggedIn();

    boolean isAdmin();

    String logout();
}
