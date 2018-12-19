package it.unical.asde.pr78.controller.student;

import it.unical.asde.pr78.controller.BaseController;
import it.unical.asde.pr78.entity.Submission;
import it.unical.asde.pr78.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class StuSubmissionController extends BaseController {

    @Autowired
    private SubmissionService submissionService;

    @GetMapping("/student/submissions")
    public ModelAndView submissions() {

        ModelAndView modelAndView = new ModelAndView("student/submissions");

        List<Submission> submissions = this.submissionService.findAllByStudent(loggedInUser());

        modelAndView.addObject("submissions", submissions);

        if (submissions.isEmpty()) {
            modelAndView.addObject("warning", "You do not have any submission");
        }

        return modelAndView;
    }
}
