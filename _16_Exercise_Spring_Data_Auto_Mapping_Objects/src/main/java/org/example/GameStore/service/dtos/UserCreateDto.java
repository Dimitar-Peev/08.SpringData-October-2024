package org.example.GameStore.service.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;

public class UserCreateDto {

    @Pattern(regexp ="^((\\w+)|(\\w+.\\w+))@\\w+\\.\\w+$", message = "Invalid email")
    @Column(unique = true)
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$" , message = "Invalid password")
    private String password;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$" , message = "Invalid confirm password")
    private String confirmPassword;

    private String fullName;

    public UserCreateDto() {
    }

    public UserCreateDto(String email, String password, String confirmPassword, String fullName) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.fullName = fullName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return this.confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
