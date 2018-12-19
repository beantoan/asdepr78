package it.unical.asde.pr78.service;

import it.unical.asde.pr78.entity.User;
import it.unical.asde.pr78.exception.InvalidUserException;
import it.unical.asde.pr78.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // TODO this is only for developing purpose. We need to change to STATUS_CREATED
        user.setStatus(User.STATUS_ACTIVE);

        this.userRepository.save(user);

        return user;
    }

    @Override
    public boolean updateProfile(User existedUser, User user) throws InvalidUserException {
        if (StringUtils.isEmpty(user.getPassword())) {
            return false;
        }

        if (user.getPassword().length() < 8) {
            throw new InvalidUserException("Your password must be greater than or equal to 8 characters.");
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new InvalidUserException("Your password and confirm password are not matched");
        }

        existedUser.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            this.userRepository.save(existedUser);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidUserException("There is a problem when saving the password. Please try again.");
        }
    }

}
