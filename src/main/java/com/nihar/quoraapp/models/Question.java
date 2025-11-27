package com.nihar.quoraapp.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "questions")
public class Question {
    @Id
    private String id;

    // Author of this question (user id)
    @Indexed
    private String authorId;

    @NotBlank(message = "Title is Required.")
    @Size(min = 10, max = 100, message = "Title must be between 10 to 100 characters.")
    private String title;

    @NotBlank(message = "Content is Required.")
    @Size(min = 10, max = 100, message = "Title must be between 10 to 100 characters.")
    private String content;

    private Integer views;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
