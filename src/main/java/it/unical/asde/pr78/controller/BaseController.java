package it.unical.asde.pr78.controller;

import it.unical.asde.pr78.entity.User;
import it.unical.asde.pr78.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseController {

    @Autowired
    protected UserService userService;

    protected final User loggedInUser() {
        String username = this.getAuthentication().getName();

        return userService.findByEmail(username);
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
