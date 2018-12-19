package it.unical.asde.pr78.service;

import it.unical.asde.pr78.entity.User;
import it.unical.asde.pr78.form.ApiResponseForm;

public interface UpdateAnswerCorrectnessService {
    ApiResponseForm execute(User professor, Long examId, Long submissionId, Long questionId, boolean isCorrect);
}
