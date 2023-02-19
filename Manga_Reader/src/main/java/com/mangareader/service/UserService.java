package com.mangareader.service;

import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Set;

public interface UserService {

    public User saveUser(User user);

    public User updateUser(User user);

    public void deleteUser(Long id);

    public User getUserById(Long id) throws ResourceNotFoundException;

    public User getUserByUsername(String username) throws ResourceNotFoundException;

    public List<User> getUsers() throws ResourceNotFoundException;

    public Boolean existsByUsername(String username);

    public User changeUserRole(Long id, RoleName roleName);

}
