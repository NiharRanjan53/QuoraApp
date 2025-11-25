package com.nihar.quoraapp.services;

import com.nihar.quoraapp.adapter.QuestionAdapter;
import com.nihar.quoraapp.adapter.UserAdapter;
import com.nihar.quoraapp.dto.UserRequestDTO;
import com.nihar.quoraapp.dto.UserResponseDTO;
import com.nihar.quoraapp.models.Question;
import com.nihar.quoraapp.models.User;
import com.nihar.quoraapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;

    @Override
    public Mono<UserResponseDTO> createUser(UserRequestDTO userRequestDTO) {
        return userRepository.existsByEmail(userRequestDTO.getEmail())
                .flatMap(emailExists -> {
//                    System.out.println("================"+emailExists);
                    if (emailExists) {
//                        System.out.println("Email exists");
                        return Mono.error(new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "Email already exists"
                        ));
                    }
                    User user = UserAdapter.toUser(userRequestDTO);
                    user.setCreatedAt(LocalDateTime.now());
                    user.setUpdatedAt(LocalDateTime.now());
                    System.out.println(user);
                    // TODO: Hash the password before saving
                    return userRepository.save(user)
                            .map(UserAdapter::toUserResponseDTO);
                })
                .doOnSuccess(response -> System.out.println("User created successfully: " + response))
                .doOnError(error -> System.out.println("Error creating user: " + error));



//        Question question =  Question.builder()
//                .title(questionRequestDto.getTitle())
//                .content(questionRequestDto.getContent())
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//        return questionRepository.save(question)
//                .flatMap(savedQuestion ->
//                        questionIndexService.createQuestionIndex(savedQuestion) // dumping the question to elasticsearch
//                                .thenReturn(QuestionAdapter.toQuestionResponseDTO(savedQuestion))
//                )
//                .doOnSuccess(response -> System.out.println("Question created successfully: " + response))
//                .doOnError(error -> System.out.println("Error catching question :" + error));
    }

    @Override
    public Mono<UserResponseDTO> getUserById(String id) {
        return userRepository.findById(id)
                .map(UserAdapter::toUserResponseDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with id: " + id)))
                .doOnError(error -> System.out.println("Error fetching user: " + error))
                .doOnSuccess(response -> System.out.println("User fetched successfully: " + response));
    }

    @Override
    public Flux<UserResponseDTO> getUsersByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserAdapter::toUserResponseDTO)
                .switchIfEmpty(Flux.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "No users found with username: " + username)))
                .doOnError(error -> System.out.println("Error fetching user: " + error))
                .doOnComplete(()-> System.out.println("User searched successfully"));
    }

    @Override
    public Mono<UserResponseDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserAdapter::toUserResponseDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with email: " + email)))
                .doOnError(error -> System.out.println("Error fetching user: " + error));
    }

    @Override
    public Flux<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .map(UserAdapter::toUserResponseDTO)
                .doOnError(error -> System.out.println("Error fetching users: " + error))
                .doOnComplete(() -> System.out.println("Users fetched successfully"));
    }

    @Override
    public Mono<UserResponseDTO> updateUser(String id, UserRequestDTO userRequestDTO) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with id: " + id)))
                .flatMap(existingUser -> {
                    // Check if username is being changed and if new username already exists
                    if (!existingUser.getUsername().equals(userRequestDTO.getUsername())) {
                        return userRepository.existsByUsername(userRequestDTO.getUsername())
                                .flatMap(usernameExists -> {
                                    if (usernameExists) {
                                        return Mono.error(new RuntimeException("Username already exists"));
                                    }
                                    return Mono.just(existingUser);
                                });
                    }
                    // Check if email is being changed and if new email already exists
                    if (!existingUser.getEmail().equals(userRequestDTO.getEmail())) {
                        return userRepository.existsByEmail(userRequestDTO.getEmail())
                                .flatMap(emailExists -> {
                                    if (emailExists) {
                                        return Mono.error(new RuntimeException("Email already exists"));
                                    }
                                    return Mono.just(existingUser);
                                });
                    }
                    return Mono.just(existingUser);
                })
                .flatMap(existingUser -> {
                    existingUser.setUsername(userRequestDTO.getUsername());
                    existingUser.setEmail(userRequestDTO.getEmail());
                    existingUser.setBio(userRequestDTO.getBio());
                    if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isEmpty()) {
                        // TODO: Hash the password before saving
                        existingUser.setPassword(userRequestDTO.getPassword());
                    }
                    existingUser.setUpdatedAt(LocalDateTime.now());
                    return userRepository.save(existingUser)
                            .map(UserAdapter::toUserResponseDTO);
                })
                .doOnSuccess(response -> System.out.println("User updated successfully: " + response))
                .doOnError(error -> System.out.println("Error updating user: " + error));
    }

    @Override
    public Mono<Void> deleteUser(String id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with id: " + id)))
                .flatMap(userRepository::delete)
                .doOnSuccess(response -> System.out.println("User deleted successfully: " + id))
                .doOnError(error -> System.out.println("Error deleting user: " + error));
    }
}

