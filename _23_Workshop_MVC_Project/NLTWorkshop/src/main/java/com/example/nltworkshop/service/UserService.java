package com.example.nltworkshop.service;

import com.example.nltworkshop.web.models.UserLoginModel;
import com.example.nltworkshop.web.models.UserRegisterModel;

public interface UserService {
    boolean validateRegisterModel(UserRegisterModel userRegisterModel);

    void registerUser(UserRegisterModel userRegisterModel);

    boolean loggedIn(UserLoginModel userLoginModel);
}