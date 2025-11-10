package com.nihar.quoraapp.services;

import com.nihar.quoraapp.models.Question;
import com.nihar.quoraapp.models.QuestionElasticDocument;
import reactor.core.publisher.Mono;

public interface IQuestionIndexService {
    Mono<QuestionElasticDocument> createQuestionIndex(Question question) ;
}
