package com.nihar.quoraapp.repositories;

import com.nihar.quoraapp.models.Answer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends ReactiveMongoRepository<Answer, String> {
}
