package it.unical.asde.pr78.service;

import it.unical.asde.pr78.entity.Exam;
import it.unical.asde.pr78.entity.Question;
import it.unical.asde.pr78.entity.Submission;
import it.unical.asde.pr78.entity.User;
import it.unical.asde.pr78.exception.InvalidExamException;
import it.unical.asde.pr78.form.ApiResponseForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateAnswerCorrectnessServiceImpl implements UpdateAnswerCorrectnessService {

    @Autowired
    private ExamService examService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private SubmissionService submissionService;

    @Override
    public ApiResponseForm execute(User professor, Long examId, Long submissionId, Long questionId, boolean isCorrect) {
        ApiResponseForm apiResponseForm = new ApiResponseForm();

        try {
            Exam exam = this.examService.findByIdAndProfessor(examId, professor);
            Submission submission = this.submissionService.findReviewing(exam, submissionId);
            Question question = this.answerService.updateCorrectness(exam, submission, questionId, isCorrect);

            String message = String.format("The answers of student '%s' for question '%s' have been marked as correct",
                    submission.getStudent().getFullName(), question.getTitle());

            apiResponseForm.setStatus(ApiResponseForm.STATUS_OK);
            apiResponseForm.setMessage(message);
        } catch (InvalidExamException e) {
            apiResponseForm.setStatus(ApiResponseForm.STATUS_ERROR);
            apiResponseForm.setMessage(e.getMessage());
        }

        return apiResponseForm;
    }
}
