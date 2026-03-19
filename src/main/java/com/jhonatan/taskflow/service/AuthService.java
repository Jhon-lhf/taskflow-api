package com.jhonatan.taskflow.service;

import com.jhonatan.taskflow.dto.AuthResponse;
import com.jhonatan.taskflow.dto.LoginRequest;
import com.jhonatan.taskflow.dto.RegisterRequest;
import com.jhonatan.taskflow.dto.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
