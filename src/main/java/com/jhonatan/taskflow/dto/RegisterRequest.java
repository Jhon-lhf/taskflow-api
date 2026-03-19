package com.jhonatan.taskflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RegisterRequest {
    @NotBlank(message = "The username mustn't be empty")
    private String username;

    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "The password mustn't be empty")
    private String password;
}
