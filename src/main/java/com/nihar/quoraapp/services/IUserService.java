package com.nihar.quoraapp.services;

import com.nihar.quoraapp.dto.UserRequestDTO;
import com.nihar.quoraapp.dto.UserResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserService {
    Mono<UserResponseDTO> createUser(UserRequestDTO userRequestDTO);
    Mono<UserResponseDTO> getUserById(String id);
    Flux<UserResponseDTO> getUsersByUsername(String username);
    Mono<UserResponseDTO> getUserByEmail(String email);
    Flux<UserResponseDTO> getAllUsers();
    Mono<UserResponseDTO> updateUser(String id, UserRequestDTO userRequestDTO);
    Mono<Void> deleteUser(String id);
}

