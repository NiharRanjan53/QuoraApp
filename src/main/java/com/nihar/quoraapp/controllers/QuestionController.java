package com.nihar.quoraapp.controllers;


import com.nihar.quoraapp.dto.QuestionRequestDTO;
import com.nihar.quoraapp.dto.QuestionResponseDTO;
import com.nihar.quoraapp.repositories.QuestionRepository;
import com.nihar.quoraapp.services.IQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class QuestionController {
    private final IQuestionService questionService;

    @PostMapping
    public Mono<QuestionResponseDTO> createQuestion(@RequestBody QuestionRequestDTO questionRequestDto){
        return questionService.createQuestion(questionRequestDto)
                .doOnSuccess(response -> System.out.println("Question created successfully: " + response))
                .doOnError(error -> System.out.println("Error catching question :" + error));
    }

    @GetMapping("/search")
    public Flux<QuestionResponseDTO> searchQuestions(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return questionService.searchQuestions(query, page, size);
    }

    @GetMapping
    public Flux<QuestionResponseDTO> getAllQuestions(
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size){
        return questionService.getAllQuestions(cursor, size);
    }}
