package it.unical.asde.pr78.service;

import it.unical.asde.pr78.entity.*;
import it.unical.asde.pr78.exception.InvalidAnswerException;

import java.util.List;
import java.util.Map;

public interface AnswerService {
    List<Answer> findAllByQuestions(List<Question> questions);

    List<Answer> submitAnswers(User student, Map<Long, String[]> answers, Submission submission)
            throws InvalidAnswerException;

    Question updateCorrectness(Exam exam, Submission submission, Long questionId, boolean isCorrect)
            throws InvalidAnswerException;
}
