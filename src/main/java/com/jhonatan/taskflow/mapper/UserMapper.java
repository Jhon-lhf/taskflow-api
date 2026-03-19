package com.jhonatan.taskflow.mapper;

import com.jhonatan.taskflow.domain.entity.User;
import com.jhonatan.taskflow.dto.UserResponse;

public class UserMapper {
    public static UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
