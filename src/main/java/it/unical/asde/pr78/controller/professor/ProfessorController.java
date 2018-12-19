package it.unical.asde.pr78.controller.professor;

import it.unical.asde.pr78.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProfessorController extends BaseController {

    @GetMapping("/professor")
    public ModelAndView dashboard() {
        return new ModelAndView("professor/dashboard");
    }
}
