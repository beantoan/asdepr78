package it.unical.asde.pr78.service;

import it.unical.asde.pr78.entity.Role;
import it.unical.asde.pr78.entity.User;
import it.unical.asde.pr78.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {

        User user = this.userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("Invalid email/password.");
        }

        if (!user.isActive()) {
            throw new UsernameNotFoundException("User is not active.");
        }

        List<Role> roles = user.getRoles();
        int rolesSize = roles.size();

        String[] roleNames = new String[rolesSize];

        for (int i = 0; i < rolesSize; i++) {
            roleNames[i] = roles.get(i).getName();
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList(roleNames));
    }
}
