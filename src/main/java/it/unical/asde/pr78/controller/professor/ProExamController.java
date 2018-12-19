package it.unical.asde.pr78.controller.professor;

import it.unical.asde.pr78.controller.BaseController;
import it.unical.asde.pr78.entity.Exam;
import it.unical.asde.pr78.entity.Question;
import it.unical.asde.pr78.exception.InvalidExamException;
import it.unical.asde.pr78.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class ProExamController extends BaseController {

    private Logger logger = Logger.getLogger(ProExamController.class.getName());

    @Autowired
    private ExamService examService;

    @GetMapping("/professor/exams")
    public ModelAndView exams() {
        ModelAndView modelAndView = new ModelAndView("professor/exams");

        List<Exam> exams = this.examService.findAllByProfessor(loggedInUser());

        modelAndView.addObject("exams", exams);

        if (exams.isEmpty()) {
            modelAndView.addObject("warning", "You do not have any exam. Create the first one by clicking Create Exam button.");
        }

        return modelAndView;
    }

    @GetMapping("/professor/exams/create")
    public ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("professor/exam");

        modelAndView.addObject("exam", new Exam());
        modelAndView.addObject("questionTypes", Question.getTypes());

        return modelAndView;
    }

    @PostMapping("/professor/exams/create")
    public ModelAndView save(@Valid @ModelAttribute("exam") Exam exam, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        logger.info(exam.toString());

        ModelAndView modelAndView = new ModelAndView();

        try {
            boolean isNew = exam.getId() == null;

            Exam savedExam = this.examService.saveOrUpdate(loggedInUser(), exam, bindingResult);

            String message;

            if (isNew) {
                modelAndView.setViewName("redirect:/professor/exams/create");

                message = String.format("The exam '%s' has been created successfully.", savedExam.getTitle());
            } else {
                modelAndView.setViewName("redirect:/professor/exams/view/" + exam.getId());

                message = String.format("The exam '%s' has been updated successfully.", savedExam.getTitle());
            }

            redirectAttributes.addFlashAttribute("success", message);
        } catch (InvalidExamException e) {
            modelAndView.addObject("hasErrors", bindingResult.hasErrors());

            modelAndView.setViewName("professor/exam");

            modelAndView.addObject("warning", e.getMessage());
            modelAndView.addObject("exam", exam);
            modelAndView.addObject("questionTypes", Question.getTypes());
        }

        return modelAndView;
    }

    @GetMapping("/professor/exams/view/{examId}")
    public ModelAndView view(@PathVariable("examId") Long examId) {
        ModelAndView modelAndView = new ModelAndView("professor/exam");

        Exam exam = this.examService.findByIdAndProfessor(examId, loggedInUser());

        modelAndView.addObject("exam", exam);
        modelAndView.addObject("questionTypes", Question.getTypes());

        if (exam == null) {
            modelAndView.addObject("warning", "The exam is not valid or not available. Create the first one by clicking Create Exam button.");
        }

        return modelAndView;
    }
}
