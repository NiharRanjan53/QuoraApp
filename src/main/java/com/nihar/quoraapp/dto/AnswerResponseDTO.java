package com.nihar.quoraapp.dto;

import com.nihar.quoraapp.enums.TargetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerResponseDTO {
    private String id;

    private String authorId;

    private String questionId;

    private String content;


    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
