package com.nihar.quoraapp.services;

import com.nihar.quoraapp.dto.QuestionRequestDTO;
import com.nihar.quoraapp.dto.QuestionResponseDTO;
import com.nihar.quoraapp.models.QuestionElasticDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IQuestionService {
    public Mono<QuestionResponseDTO> createQuestion(QuestionRequestDTO questionRequestDto);
    public Flux<QuestionResponseDTO> searchQuestions(String searchTerm, int offset, int page);
    public Flux<QuestionResponseDTO> getAllQuestions(String cursor, int size);
    public Mono<QuestionResponseDTO> getQuestionById(String id);
    public List<QuestionElasticDocument> searchQuestionsByElasticsearch(String query);
}
