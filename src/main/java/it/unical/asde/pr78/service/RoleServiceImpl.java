package it.unical.asde.pr78.service;

import it.unical.asde.pr78.entity.Role;
import it.unical.asde.pr78.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Override
    public Role findById(Long id) {
        return this.roleRepository.findById(id).get();
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
