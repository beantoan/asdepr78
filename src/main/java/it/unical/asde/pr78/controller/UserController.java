package it.unical.asde.pr78.controller;

import it.unical.asde.pr78.entity.Role;
import it.unical.asde.pr78.entity.User;
import it.unical.asde.pr78.exception.InvalidUserException;
import it.unical.asde.pr78.service.RoleService;
import it.unical.asde.pr78.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/")
    public String authenticated(HttpServletRequest httpServletRequest) {
        if (httpServletRequest.isUserInRole(Role.ROLE_SECRETARY)) {
            return "redirect:/secretary";
        } else if (httpServletRequest.isUserInRole(Role.ROLE_PROFESSOR)) {
            return "redirect:/professor";
        } else {
            return "redirect:/student";
        }
    }

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(value = "error", required = false) Boolean error,
                              @RequestParam(value = "logout", required = false) String logout) {
        ModelAndView modelAndView = new ModelAndView("login");

        modelAndView.addObject("user", new User());

        if (error != null && error) {
            modelAndView.addObject("warning", "You have entered an invalid username or password");
        }

        if (logout != null) {
            modelAndView.addObject("success", "You have logged out successfully");
        }

        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView register() {
        return this.buildModelAndViewForRegister(null);
    }

    @PostMapping("/register")
    public ModelAndView create(@Validated @ModelAttribute("user") User user, BindingResult bindingResult) {
        if (!bindingResult.hasErrors() && this.userService.createUser(user) != null) {
            return new ModelAndView("redirect:/login");
        } else {
            user.setPassword(null);
            return this.buildModelAndViewForRegister(user);
        }
    }

    private ModelAndView buildModelAndViewForRegister(User user) {
        if (user == null) {
            user = new User();
        }

        ModelAndView modelAndView = new ModelAndView("register");

        modelAndView.addObject("user", user);
        modelAndView.addObject("roles", this.roleService.findAll());

        return modelAndView;
    }

    @GetMapping("/profile")
    public ModelAndView profile() {
        ModelAndView modelAndView = new ModelAndView("profile");

        User user = this.userForEditing(loggedInUser());

        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @PostMapping("/profile")
    public ModelAndView updateProfile(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();

        User loggedInUser = loggedInUser();

        try {
            boolean isUpdated = this.userService.updateProfile(loggedInUser, user);

            modelAndView.setViewName("redirect:/profile");

            if (isUpdated) {
                redirectAttributes.addFlashAttribute("success", "Your password is changed successfully.");
            }
        } catch (InvalidUserException e) {
            user = this.userForEditing(loggedInUser);

            modelAndView.addObject("warning", e.getMessage());
            modelAndView.addObject("user", user);
        }

        return modelAndView;
    }

    private User userForEditing(User existedUser) {
        User user = new User();
        user.setFullName(existedUser.getFullName());
        user.setEmail(existedUser.getEmail());

        return user;
    }
}
