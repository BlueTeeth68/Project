package com.mangareader.service;

import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.exception.ResourceNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserService {

    public User saveUser(User user);

    public User updateUser(User user);

    public void deleteUser(Long id);

    public User getUserById(Long id) throws ResourceNotFoundException;

    public User getUserByUsername(String username) throws ResourceNotFoundException;

    public List<User> getUsersByActivateStatus(Boolean activate) throws ResourceNotFoundException;

    public List<User> getUsers() throws ResourceNotFoundException;

    public Boolean existsByUsername(String username);

    public User changeUserRole(Long id, RoleName roleName);

    public User changeDisplayName(Long id, String displayName);

    public User updateAvatar(User user, MultipartFile file);

    public Resource getAvatar(String fileName);

    public void deleteUserById(Long id);

}
