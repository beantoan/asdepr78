package it.unical.asde.pr78.controller.professor;

import it.unical.asde.pr78.controller.BaseController;
import it.unical.asde.pr78.entity.Exam;
import it.unical.asde.pr78.entity.Submission;
import it.unical.asde.pr78.exception.InvalidExamException;
import it.unical.asde.pr78.service.ExamService;
import it.unical.asde.pr78.service.ReviewSubmissionService;
import it.unical.asde.pr78.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.logging.Logger;

@Controller
public class ProSubmissionController extends BaseController {

    private Logger logger = Logger.getLogger(ProSubmissionController.class.getName());

    @Autowired
    private ExamService examService;

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private ReviewSubmissionService reviewSubmissionService;

    @GetMapping("/professor/submissions/{examId}")
    public ModelAndView submissions(@PathVariable("examId") Long examId) {
        ModelAndView modelAndView = new ModelAndView("professor/submissions");

        try {
            Exam exam = this.examService.findByIdAndProfessor(examId, loggedInUser());
            List<Submission> submissions = this.submissionService.findAllByExam(exam);

            modelAndView.addObject("exam", exam);
            modelAndView.addObject("submissions", submissions);

            if (submissions.isEmpty()) {
                modelAndView.addObject("warning", "There is no submission for this exam");
            }
        } catch (InvalidExamException e) {
            logger.warning(e.getMessage());

            modelAndView.addObject("warning", e.getMessage());
        }

        return modelAndView;
    }

    @GetMapping("/professor/submissions/review/{examId}/{submissionId}")
    public ModelAndView review(@PathVariable("examId") Long examId,
                               @PathVariable("submissionId") Long submissionId) {

        ModelAndView modelAndView = this.reviewSubmissionService.review(loggedInUser(), examId, submissionId);

        modelAndView.setViewName("professor/submission");

        return modelAndView;
    }

    @GetMapping("/professor/submissions/view/{examId}/{submissionId}")
    public ModelAndView view(@PathVariable("examId") Long examId,
                                   @PathVariable("submissionId") Long submissionId) {

        ModelAndView modelAndView = this.reviewSubmissionService.view(loggedInUser(), examId, submissionId);

        modelAndView.setViewName("professor/submission");

        return modelAndView;
    }
}
