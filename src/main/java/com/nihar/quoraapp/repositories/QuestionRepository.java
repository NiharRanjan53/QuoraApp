package com.nihar.quoraapp.repositories;

import com.nihar.quoraapp.models.Question;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface QuestionRepository extends ReactiveMongoRepository<Question, String> {

}
