package com.nihar.quoraapp.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collation = "questions")
public class Question {
    @Id
    private String id;

    @NotBlank(message = "Title is Required.")
    @Size(min = 10, max = 100, message = "Title must be between 10 to 100 characters.")
    private String title;

    @NotBlank(message = "Content is Required.")
    @Size(min = 10, max = 100, message = "Title must be between 10 to 100 characters.")
    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
