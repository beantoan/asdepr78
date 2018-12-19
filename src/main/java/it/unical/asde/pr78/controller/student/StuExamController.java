package it.unical.asde.pr78.controller.student;

import it.unical.asde.pr78.controller.BaseController;
import it.unical.asde.pr78.entity.Answer;
import it.unical.asde.pr78.entity.Exam;
import it.unical.asde.pr78.entity.Submission;
import it.unical.asde.pr78.entity.User;
import it.unical.asde.pr78.exception.InvalidAnswerException;
import it.unical.asde.pr78.exception.InvalidExamException;
import it.unical.asde.pr78.form.AnswerForm;
import it.unical.asde.pr78.service.AnswerService;
import it.unical.asde.pr78.service.ExamService;
import it.unical.asde.pr78.service.SubmissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.logging.Logger;

@Controller
public class StuExamController extends BaseController {

    private Logger logger = Logger.getLogger(StuExamController.class.getName());

    @Autowired
    private ExamService examService;

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private AnswerService answerService;

    @GetMapping("/student/exams")
    public ModelAndView exams(@ModelAttribute("success") String success) {
        ModelAndView modelAndView = new ModelAndView("student/exams");

        List<Submission> submissions = this.submissionService.findAllByStudent(loggedInUser());
        List<Exam> exams = this.examService.findAllAvailableForStudent(submissions);

        if (exams.isEmpty() && StringUtils.isBlank(success)) {
            String message = "There is no opening exam now. Please wait for the news from your professors.";
            modelAndView.addObject("warning", message);
        } else {
            modelAndView.addObject("success", success);
        }

        modelAndView.addObject("exams", exams);

        return modelAndView;
    }

    @GetMapping("/student/exams/start/{id}")
    public ModelAndView start(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("student/exam");

        try {
            Submission submission = this.submissionService.createOrContinue(loggedInUser(), id);

            modelAndView.addObject("exam", submission.getExam());
            modelAndView.addObject("duration", submission.getRemainingTime());
            modelAndView.addObject("answerForm", new AnswerForm());
        } catch (InvalidExamException e) {
            logger.warning(e.getMessage());

            modelAndView.addObject("warning", e.getMessage());
        }

        return modelAndView;
    }

    @PostMapping("/student/exams/submit/{id}")
    public ModelAndView submit(@ModelAttribute("answerForm") AnswerForm answerForm,
                               @PathVariable("id") Long id,
                               RedirectAttributes redirectAttributes) {
        logger.info(answerForm.toString());

        try {
            User student = loggedInUser();
            Submission submission = this.submissionService.getValidSubmission(student, id);
            List<Answer> anwers = this.answerService.submitAnswers(student, answerForm.getAnswers(), submission);

            String message = String.format("Congratulation!!! You have completed the exam '%s'. The score will be informed later.", submission.getExam().getTitle());

            redirectAttributes.addFlashAttribute("success", message);

            return new ModelAndView("redirect:/student/exams");
        } catch (InvalidAnswerException e) {
            logger.warning(e.getMessage());

            ModelAndView modelAndView = new ModelAndView("student/exam");

            modelAndView.addObject("warning", e.getMessage());
            modelAndView.addObject("exam", this.examService.findById(id));
            modelAndView.addObject("answerForm", answerForm);

            return modelAndView;
        }
    }
}
