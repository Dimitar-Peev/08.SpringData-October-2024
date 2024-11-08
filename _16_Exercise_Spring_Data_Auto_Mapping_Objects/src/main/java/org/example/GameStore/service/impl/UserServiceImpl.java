package org.example.GameStore.service.impl;

import jakarta.validation.ConstraintViolation;
import org.example.GameStore.data.entities.User;
import org.example.GameStore.data.repositories.UserRepository;
import org.example.GameStore.service.UserService;
import org.example.GameStore.service.dtos.UserCreateDto;
import org.example.GameStore.service.dtos.UserLoginDto;
import org.example.GameStore.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private User user;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public String registerUser(UserCreateDto userCreateDto) {
        if (!userCreateDto.getPassword().equals(userCreateDto.getConfirmPassword())) {
            return "Passwords do not match";
        }

        if (!validationUtil.isValid(userCreateDto)) {
            return validationUtil.validate(userCreateDto).stream().map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(System.lineSeparator()));
        }

        if (this.userRepository.findByEmail(userCreateDto.getEmail()).isPresent()) {
            return "Email address already in use";
        }

        User user = this.modelMapper.map(userCreateDto, User.class);
        setRootUserAdmin(user);
        this.userRepository.saveAndFlush(user);
        return String.format("%s was registered", user.getFullName());
    }

    @Override
    public String loginUser(UserLoginDto userLoginDto) {
        Optional<User> user = this.userRepository.findByEmailAndPassword(userLoginDto.getEmail(), userLoginDto.getPassword());

        if (user.isEmpty()) {
            return "Invalid username or password";
        } else {
            this.user = user.get();
            return String.format("Successfully logged in %s", user.get().getFullName());
        }
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public boolean isLoggedIn() {
        return this.user != null;
    }

    @Override
    public boolean isAdmin() {
        return this.user != null && this.user.isAdmin();
    }

    @Override
    public String logout() {
        if (this.isLoggedIn()) {
            String text = String.format("User %s successfully logged out", this.user.getFullName());
            this.user = null;
            return text;
        }
        return "No logged in user";
    }

    private void setRootUserAdmin(User user) {
        if (this.userRepository.count() == 0) {
            user.setAdmin(true);
        }
    }
}
