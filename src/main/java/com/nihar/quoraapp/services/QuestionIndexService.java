package com.nihar.quoraapp.services;

import com.nihar.quoraapp.models.Question;
import com.nihar.quoraapp.models.QuestionElasticDocument;
import com.nihar.quoraapp.repositories.QuestionDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class QuestionIndexService implements IQuestionIndexService {
    private final QuestionDocumentRepository questionDocumentRepository;
    private final ReactiveElasticsearchOperations ops;
    @Override
    public Mono<QuestionElasticDocument> createQuestionIndex(Question question) {
        QuestionElasticDocument document =  QuestionElasticDocument.builder()
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .build();

        // questionDocumentRepository.save(document);

        // Save to ES, then refresh index so searches see it immediately (dev convenience)
        return questionDocumentRepository.save(document)
                .flatMap(saved ->
                        ops.indexOps(QuestionElasticDocument.class).refresh()
                                .thenReturn(saved)
                );
    }
}
