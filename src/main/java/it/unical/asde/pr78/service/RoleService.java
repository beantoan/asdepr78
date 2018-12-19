package it.unical.asde.pr78.service;

import it.unical.asde.pr78.entity.Role;

import java.util.List;

public interface RoleService {
    Role findById(Long id);

    List<Role> findAll();
}
