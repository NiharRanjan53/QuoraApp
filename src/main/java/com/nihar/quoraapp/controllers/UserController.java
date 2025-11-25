package com.nihar.quoraapp.controllers;

import com.nihar.quoraapp.dto.UserRequestDTO;
import com.nihar.quoraapp.dto.UserResponseDTO;
import com.nihar.quoraapp.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final IUserService userService;

    @PostMapping
    public Mono<ResponseEntity<UserResponseDTO>> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        return userService.createUser(userRequestDTO)
                .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponseDTO>> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(user))
                .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @GetMapping("/username/{username}")
    public Flux<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        return userService.getUsersByUsername(username);
    }

    @GetMapping("/email/{email}")
    public Mono<ResponseEntity<UserResponseDTO>> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(user -> ResponseEntity.ok(user))
                .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @GetMapping
    public Flux<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserResponseDTO>> updateUser(
            @PathVariable String id,
            @RequestBody UserRequestDTO userRequestDTO) {
        return userService.updateUser(id, userRequestDTO)
                .map(user -> ResponseEntity.ok(user))
                .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable String id) {
        return userService.deleteUser(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }
}
