package it.unical.asde.pr78.service;

import it.unical.asde.pr78.entity.User;
import it.unical.asde.pr78.form.ApiResponseForm;
import org.springframework.web.servlet.ModelAndView;

public interface ReviewSubmissionService {
    ModelAndView review(User professor, Long examId, Long submissionId);

    ModelAndView view(User professor, Long examId, Long submissionId);

    ApiResponseForm markAsReviewed(User professor, Long examId, Long submissionId);
}
