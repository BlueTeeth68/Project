package com.mangareader.service;

import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.exception.ResourceNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserService {

    User saveUser(User user);

    User updateUser(User user);

    void deleteUser(Long id);

    User getUserById(Long id) throws ResourceNotFoundException;

    User getUserByUsername(String username) throws ResourceNotFoundException;

    List<User> getUsersByActivateStatus(Boolean activate) throws ResourceNotFoundException;

    List<User> getUsers() throws ResourceNotFoundException;

    List<User> getAllAndPaginateUsers(int limit, int offset);

    Boolean existsByUsername(String username);

    User changeUserRole(Long id, RoleName roleName);

    User changeDisplayName(Long id, String displayName);

    User updateAvatar(Long id, MultipartFile file);

    Resource getAvatar(String fileName);

    void deleteUserById(Long id);

}
