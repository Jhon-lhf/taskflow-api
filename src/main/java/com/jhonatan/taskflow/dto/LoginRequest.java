package com.jhonatan.taskflow.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "The username mustn't be empty")
    private String username;

    @NotBlank(message = "The password mustn't be empty")
    private String password;
}
