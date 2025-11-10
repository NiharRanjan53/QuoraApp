package com.nihar.quoraapp.repositories;

import com.nihar.quoraapp.models.QuestionElasticDocument;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface QuestionDocumentRepository extends ReactiveElasticsearchRepository<QuestionElasticDocument, String> {
        Flux<QuestionElasticDocument> findByTitleContainingOrContentContaining(String title, String content);

}
