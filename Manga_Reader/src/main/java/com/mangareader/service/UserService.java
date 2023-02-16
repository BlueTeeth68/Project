package com.mangareader.service;

import com.mangareader.domain.Role;
import com.mangareader.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {

    public User saveUser(User user);

    public User updateUser(User user);

    public void deleteUser(Long id);

    public User getUserById(Long id);

    public User getUserByUsername(String username);

    public List<User> getUsers();

    public Role saveRole(Role role);

    public Role updateRole(Role role);

    public void deleteRole(Long id);

    public List<Role> getRoles();

    public Role getRoleById(Long id);

    public Role getRoleByName(String name);
    public User addRoleToUser(String username, String roleName);
}
