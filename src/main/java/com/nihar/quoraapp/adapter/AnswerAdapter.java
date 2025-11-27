package com.nihar.quoraapp.adapter;

import com.nihar.quoraapp.dto.AnswerResponseDTO;
import com.nihar.quoraapp.dto.QuestionResponseDTO;
import com.nihar.quoraapp.models.Answer;
import com.nihar.quoraapp.models.Question;

public class AnswerAdapter {
    public static AnswerResponseDTO toAnswerResponseDTO(Answer answer){
        return AnswerResponseDTO.builder()
                .id(answer.getId())
                .authorId(answer.getAuthorId())
                .questionId(answer.getQuestionId())
                .content(answer.getContent())
                .createdAt(answer.getCreatedAt())
                .build();
    }

}
