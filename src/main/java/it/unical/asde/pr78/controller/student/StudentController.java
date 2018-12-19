package it.unical.asde.pr78.controller.student;

import it.unical.asde.pr78.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController extends BaseController {

    @GetMapping("/student")
    public String dashboard() {
        return "student/dashboard";
    }
}
