package com.nihar.quoraapp.adapter;

import com.nihar.quoraapp.dto.QuestionResponseDTO;
import com.nihar.quoraapp.models.Question;

public class QuestionAdapter {

    public static QuestionResponseDTO toQuestionResponseDTO(Question question){
        return QuestionResponseDTO.builder()
                .id(question.getId())
                .authorId(question.getAuthorId())
                .title(question.getTitle())
                .content(question.getContent())
                .createdAt(question.getCreatedAt())
                .build();
    }

}
