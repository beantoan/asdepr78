package it.unical.asde.pr78.controller.secretary;

import it.unical.asde.pr78.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecretaryController extends BaseController {

    @GetMapping("/secretary")
    public String dashboard() {
        return "secretary/dashboard";
    }
}
