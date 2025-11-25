package com.nihar.quoraapp.adapter;

import com.nihar.quoraapp.dto.UserRequestDTO;
import com.nihar.quoraapp.dto.UserResponseDTO;
import com.nihar.quoraapp.models.User;

public class UserAdapter {

    public static UserResponseDTO toUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static User toUser(UserRequestDTO userRequestDTO) {
        return User.builder()
                .username(userRequestDTO.getUsername())
                .email(userRequestDTO.getEmail())
                .password(userRequestDTO.getPassword()) // In production, hash this password
                .bio(userRequestDTO.getBio())
                .build();
    }
}

