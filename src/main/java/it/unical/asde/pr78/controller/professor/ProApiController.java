package it.unical.asde.pr78.controller.professor;

import it.unical.asde.pr78.controller.BaseController;
import it.unical.asde.pr78.form.ApiResponseForm;
import it.unical.asde.pr78.service.ReviewSubmissionService;
import it.unical.asde.pr78.service.UpdateAnswerCorrectnessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProApiController extends BaseController {

    @Autowired
    private UpdateAnswerCorrectnessService updateAnswerCorrectnessService;

    @Autowired
    private ReviewSubmissionService reviewSubmissionService;

    @PostMapping("/professor/apis/submissionAsReviewed/{examId}/{submissionId}")
    public ApiResponseForm submissionAsReviewed(@PathVariable("examId") Long examId,
                                                @PathVariable("submissionId") Long submissionId) {

        return this.reviewSubmissionService.markAsReviewed(loggedInUser(), examId, submissionId);
    }

    @PostMapping("/professor/apis/answerAsCorrect/{examId}/{submissionId}/{questionId}")
    public ApiResponseForm answerAsCorrect(@PathVariable("examId") Long examId,
                                           @PathVariable("submissionId") Long submissionId,
                                           @PathVariable("questionId") Long questionId) {
        return this.updateAnswerCorrectnessService.execute(loggedInUser(), examId, submissionId, questionId, true);
    }

    @PostMapping("/professor/apis/answerAsIncorrect/{examId}/{submissionId}/{questionId}")
    public ApiResponseForm answerAsIncorrect(@PathVariable("examId") Long examId,
                                             @PathVariable("submissionId") Long submissionId,
                                             @PathVariable("questionId") Long questionId) {
        return this.updateAnswerCorrectnessService.execute(loggedInUser(), examId, submissionId, questionId, false);
    }
}
