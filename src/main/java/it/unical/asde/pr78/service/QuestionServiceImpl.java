package it.unical.asde.pr78.service;

import it.unical.asde.pr78.entity.Answer;
import it.unical.asde.pr78.entity.Question;
import it.unical.asde.pr78.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Service
public class QuestionServiceImpl implements QuestionService {

    private Logger logger = Logger.getLogger(QuestionServiceImpl.class.getName());

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public void mapAnswersToQuestions(List<Question> questions, List<Answer> answers) {
        questions.forEach(question -> {
            Answer[] subAnswers = answers.stream()
                    .filter(a -> a.getQuestion().equals(question))
                    .toArray(Answer[]::new);
            question.setAnswers(Arrays.asList(subAnswers));
        });
    }
}
