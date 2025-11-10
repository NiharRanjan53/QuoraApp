package com.nihar.quoraapp.services;

import com.nihar.quoraapp.models.Question;
import com.nihar.quoraapp.models.QuestionElasticDocument;
import com.nihar.quoraapp.repositories.QuestionDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionIndexService implements IQuestionIndexService {
    private final QuestionDocumentRepository questionDocumentRepository;
    @Override
    public void createQuestionIndex(Question question) {
        QuestionElasticDocument document =  QuestionElasticDocument.builder()
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .build();

        questionDocumentRepository.save(document);

    }
}
