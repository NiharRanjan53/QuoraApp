package com.nihar.quoraapp.services;

import com.nihar.quoraapp.dto.QuestionRequestDTO;
import com.nihar.quoraapp.dto.QuestionResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IQuestionService {
    public Mono<QuestionResponseDTO> createQuestion(QuestionRequestDTO questionRequestDto);
    public Flux<QuestionResponseDTO> searchQuestions(String searchTerm, int offset, int page);
}
