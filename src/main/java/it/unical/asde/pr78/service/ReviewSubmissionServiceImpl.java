package it.unical.asde.pr78.service;

import it.unical.asde.pr78.entity.*;
import it.unical.asde.pr78.exception.InvalidExamException;
import it.unical.asde.pr78.form.ApiResponseForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.logging.Logger;

@Service
public class ReviewSubmissionServiceImpl implements ReviewSubmissionService {
    private Logger logger = Logger.getLogger(ReviewSubmissionServiceImpl.class.getName());

    @Autowired
    private ExamService examService;

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionService questionService;

    @Override
    public ModelAndView review(User professor, Long examId, Long submissionId) {
        return this.reviewOrView(professor, examId, submissionId, true);
    }

    @Override
    public ModelAndView view(User professor, Long examId, Long submissionId) {
        return this.reviewOrView(professor, examId, submissionId, false);
    }

    private ModelAndView reviewOrView(User professor, Long examId, Long submissionId, boolean toReview) {
        ModelAndView modelAndView = new ModelAndView();

        try {
            Exam exam = this.examService.findByIdAndProfessor(examId, professor);

            Submission submission;

            if (toReview) {
                submission = this.submissionService.findForProfessorToReview(exam, submissionId);
            } else {
                submission = this.submissionService.findReviewed(exam, submissionId);
            }

            List<Question> questions = exam.getQuestions();

            List<Answer> answers = this.answerService.findAllByQuestions(questions);

            this.questionService.mapAnswersToQuestions(questions, answers);

            if (toReview) {
                this.submissionService.markAsReviewing(exam, submission);
            }

            modelAndView.addObject("submission", submission);
            modelAndView.addObject("exam", exam);
            modelAndView.addObject("student", submission.getStudent());
            modelAndView.addObject("questions", questions);
        } catch (InvalidExamException e) {
            logger.warning(e.getMessage());

            modelAndView.addObject("warning", e.getMessage());
        }

        return modelAndView;
    }

    @Override
    public ApiResponseForm markAsReviewed(User professor, Long examId, Long submissionId) {
        ApiResponseForm apiResponseForm = new ApiResponseForm();

        try {
            Exam exam = this.examService.findByIdAndProfessor(examId, professor);
            Submission submission = this.submissionService.findReviewing(exam, submissionId);
            this.submissionService.markAsReviewed(exam, submission);

            String message = String.format("The submission of student '%s' have been marked as reviewed",
                    submission.getStudent().getFullName());

            apiResponseForm.setStatus(ApiResponseForm.STATUS_OK);
            apiResponseForm.setMessage(message);

        } catch (InvalidExamException e) {
            apiResponseForm.setStatus(ApiResponseForm.STATUS_ERROR);
            apiResponseForm.setMessage(e.getMessage());
        }

        return apiResponseForm;
    }
}
