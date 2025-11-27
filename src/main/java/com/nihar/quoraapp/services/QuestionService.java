package com.nihar.quoraapp.services;

import com.nihar.quoraapp.adapter.QuestionAdapter;
import com.nihar.quoraapp.dto.QuestionRequestDTO;
import com.nihar.quoraapp.dto.QuestionResponseDTO;
import com.nihar.quoraapp.enums.TargetType;
import com.nihar.quoraapp.events.ViewCountEvent;
import com.nihar.quoraapp.models.Question;
import com.nihar.quoraapp.models.QuestionElasticDocument;
import com.nihar.quoraapp.producers.KafkaEventProducer;
import com.nihar.quoraapp.repositories.QuestionDocumentRepository;
import com.nihar.quoraapp.repositories.QuestionRepository;
import com.nihar.quoraapp.repositories.UserRepository;
import com.nihar.quoraapp.utils.CursorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService implements IQuestionService {
    private final QuestionRepository questionRepository;
    private final KafkaEventProducer kafkaEventProducer;
    private final QuestionIndexService questionIndexService;
    private final QuestionDocumentRepository questionDocumentRepository;
    private final UserRepository userRepository;

    @Override
    public Mono<QuestionResponseDTO> createQuestion(QuestionRequestDTO questionRequestDto) {
        // Ensure author exists
        return userRepository.findById(questionRequestDto.getAuthorId())
                .switchIfEmpty(Mono
                        .error(new RuntimeException("Author not found with id: " + questionRequestDto.getAuthorId())))
                .flatMap(user -> {
                    Question question = Question.builder()
                            .authorId(questionRequestDto.getAuthorId())
                            .title(questionRequestDto.getTitle())
                            .content(questionRequestDto.getContent())
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    return questionRepository.save(question)
                            .flatMap(savedQuestion -> questionIndexService.createQuestionIndex(savedQuestion) // dumping
                                                                                                              // the
                                                                                                              // question
                                                                                                              // to
                                                                                                              // elasticsearch
                                    .thenReturn(QuestionAdapter.toQuestionResponseDTO(savedQuestion)));
                })
                .doOnSuccess(response -> System.out.println("Question created successfully: " + response))
                .doOnError(error -> System.out.println("Error catching question :" + error));
    }

    @Override
    public Flux<QuestionResponseDTO> searchQuestions(String searchTerm, int offset, int page) {
        return questionRepository.findByTitleOrContentContainingIgnoreCase(searchTerm, PageRequest.of(offset, page))
                .map(QuestionAdapter::toQuestionResponseDTO)
                .doOnError(error -> System.out.println("Error searching questions: " + error))
                .doOnComplete(() -> System.out.println("Question searched successfully"));
    }

    @Override
    public Flux<QuestionResponseDTO> getAllQuestions(String cursor, int size) {
        Pageable pageable = PageRequest.of(0, size);
        if (!CursorUtils.isValidCursor(cursor)) {
            return questionRepository.findTop10ByOrderByCreatedAtAsc()
                    .take(size)
                    .map(QuestionAdapter::toQuestionResponseDTO)
                    .doOnError(error -> System.out.println("Error Fetching questions: " + error))
                    .doOnComplete(() -> System.out.println("Question Fetched successfully"));

        } else {
            LocalDateTime cursorTimeStamp = CursorUtils.parseCursor(cursor);
            return questionRepository.findByCreatedAtGreaterThanOrderByCreatedAtAsc(cursorTimeStamp, pageable)
                    .map(QuestionAdapter::toQuestionResponseDTO)
                    .doOnError(error -> System.out.println("Error Fetching questions: " + error))
                    .doOnComplete(() -> System.out.println("Question Fetched successfully"));
        }

    }

    @Override
    public Mono<QuestionResponseDTO> getQuestionById(String id) {
        return questionRepository.findById(id)
                .map(QuestionAdapter::toQuestionResponseDTO)
                .doOnError(error -> System.out.println("Error fetching question: " + error))
                .doOnSuccess(response -> {
                    System.out.println("Question fetched successfully: " + response);
                    ViewCountEvent viewCountEvent = new ViewCountEvent(id, TargetType.QUESTION, LocalDateTime.now());
                    kafkaEventProducer.publishViewCountEvent(viewCountEvent);
                });
    }

    @Override
    public Flux<QuestionElasticDocument> searchQuestionsByElasticsearch(String query) {
        return questionDocumentRepository.findByTitleContainingOrContentContaining(query, query);
    }
}
