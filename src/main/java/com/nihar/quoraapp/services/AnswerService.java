package com.nihar.quoraapp.services;

import com.nihar.quoraapp.adapter.AnswerAdapter;
import com.nihar.quoraapp.dto.AnswerRequestDTO;
import com.nihar.quoraapp.dto.AnswerResponseDTO;
import com.nihar.quoraapp.models.Answer;
import com.nihar.quoraapp.repositories.AnswerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class AnswerService implements IAnswerService{
    private final AnswerRepository answerRepository;

    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Override
    public Mono<AnswerResponseDTO> createAnswer(AnswerRequestDTO dto) {
        // Map DTO -> Entity
        Answer entity = Answer.builder()
                .content(dto.getContent())
                .questionId(dto.getQuestionId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return answerRepository.save(entity)
                .map(AnswerAdapter::toAnswerResponseDTO)
                .doOnError(error -> System.out.println("Error creating answers: " + error))
                .doOnSuccess(response -> System.out.println("Answer created successfully: " + response));
    }


    @Override
    public Mono<AnswerResponseDTO> getAnswerById(String id) {
        return answerRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found")))
                .map(AnswerAdapter::toAnswerResponseDTO);
    }
}
