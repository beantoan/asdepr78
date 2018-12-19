package it.unical.asde.pr78.service;

import it.unical.asde.pr78.entity.User;
import it.unical.asde.pr78.exception.InvalidUserException;

public interface UserService {
    User findByEmail(String email);

    User createUser(User user);

    boolean updateProfile(User existedUser, User user) throws InvalidUserException;
}
