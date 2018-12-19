package it.unical.asde.pr78.service;

import it.unical.asde.pr78.entity.Answer;
import it.unical.asde.pr78.entity.Question;

import java.util.List;

public interface QuestionService {
    void mapAnswersToQuestions(List<Question> questions, List<Answer> answers);
}
