package com.nihar.quoraapp.services;


import com.nihar.quoraapp.dto.AnswerRequestDTO;
import com.nihar.quoraapp.dto.AnswerResponseDTO;
import reactor.core.publisher.Mono;

public interface IAnswerService {
    public Mono<AnswerResponseDTO> createAnswer(AnswerRequestDTO answerRequestDTO);

    public Mono<AnswerResponseDTO> getAnswerById(String id);
}
