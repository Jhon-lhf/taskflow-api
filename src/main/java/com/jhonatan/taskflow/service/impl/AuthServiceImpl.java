package com.jhonatan.taskflow.service.impl;

import com.jhonatan.taskflow.domain.entity.Role;
import com.jhonatan.taskflow.domain.entity.User;
import com.jhonatan.taskflow.dto.AuthResponse;
import com.jhonatan.taskflow.dto.LoginRequest;
import com.jhonatan.taskflow.dto.RegisterRequest;
import com.jhonatan.taskflow.dto.UserResponse;
import com.jhonatan.taskflow.exception.EmailAlreadyExistsException;
import com.jhonatan.taskflow.exception.UsernameAlreadyExistsException;
import com.jhonatan.taskflow.mapper.UserMapper;
import com.jhonatan.taskflow.repository.RoleRepository;
import com.jhonatan.taskflow.repository.UserRepository;
import com.jhonatan.taskflow.security.JwtService;
import com.jhonatan.taskflow.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Override
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException(request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        Role developerRole = roleRepository.findByName("DEVELOPER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(developerRole))
                .build();

        userRepository.save(user);

        return UserMapper.toUserResponse(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        String jwt = jwtService.generateToken(userDetails);
        return AuthResponse.builder().token(jwt).build();
    }

}