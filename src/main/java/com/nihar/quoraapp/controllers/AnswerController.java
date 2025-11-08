package com.nihar.quoraapp.controllers;

import com.nihar.quoraapp.dto.AnswerRequestDTO;
import com.nihar.quoraapp.dto.AnswerResponseDTO;
import com.nihar.quoraapp.services.IAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
public class AnswerController {
    private final IAnswerService answerService;

    @PostMapping
    public Mono<AnswerResponseDTO> create(@RequestBody AnswerRequestDTO request) {
        return answerService.createAnswer(request)
                .doOnSuccess(response -> System.out.println("Answer created successfully: " + response))
                .doOnError(error -> System.out.println("Error catching answer :" + error));
    }
    @GetMapping("/{id}")
    public Mono<AnswerResponseDTO> getById(@PathVariable String id) {
        return answerService.getAnswerById(id);
    }
}
