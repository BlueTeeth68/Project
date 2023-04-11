package com.mangareader.service;

import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.web.rest.vm.ChangePasswordVM;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("unused")
public interface IUserService {

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Long id);

    User getUserById(Long id) throws ResourceNotFoundException;

    User getUserById(String id) throws ResourceNotFoundException;

    User getUserByUsername(String username) throws ResourceNotFoundException;

    User getUserByDisplayName(String displayName) throws ResourceNotFoundException;

    List<User> getUsersByActivateStatus(Boolean activate) throws ResourceNotFoundException;

    List<User> getUsers() throws ResourceNotFoundException;

    Page<User> getAllUsersWithPageable(int page, int size);

    Boolean existsByUsername(String username);

    User changeUserRole(Long id, RoleName roleName);

    User changeDisplayName(String displayName);

    User updateAvatar(MultipartFile file) throws TimeoutException;

    void deleteUserById(Long id);

    User getCurrentUser();

    User setRoleToUser(Long userId, RoleName roleName);

    User changeUserStatus(Long userId, Boolean status);

    User changePassword(ChangePasswordVM vm);

    boolean isUserLogin();

}
