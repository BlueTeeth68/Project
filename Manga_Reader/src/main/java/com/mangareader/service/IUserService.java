package com.mangareader.service;

import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.exception.ResourceNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserService {

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Long id);

    User getUserById(Long id) throws ResourceNotFoundException;

    User getUserById(String id) throws ResourceNotFoundException;

    User getUserByUsername(String username) throws ResourceNotFoundException;

    List<User> getUsersByActivateStatus(Boolean activate) throws ResourceNotFoundException;

    List<User> getUsers() throws ResourceNotFoundException;

    List<User> getAllAndPaginateUsers(int limit, int offset);
    List<User> getAllAndPaginateUsers(String limit, String page);

    Boolean existsByUsername(String username);

    User changeUserRole(Long id, RoleName roleName);

    User changeDisplayName(String displayName, String serverName);

    User updateAvatar(MultipartFile file, String serverName);

    Resource getAvatar(String fileName);

    void deleteUserById(Long id);

    User getCurrentUser();

    User addServerNameToAvatarURL(User user, String serverName);

    List<User> addServerNameToAvatarURL(List<User> users, String serverName);

    User setRoleToUser(Long userId, RoleName roleName, String serverName);

    User changeUserStatus(Long userId, Boolean status, String serverName);

}
