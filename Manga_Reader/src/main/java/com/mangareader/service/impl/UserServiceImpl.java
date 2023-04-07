package com.mangareader.service.impl;

import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.exception.BadRequestException;
import com.mangareader.exception.DataAlreadyExistsException;
import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.repository.UserRepository;
import com.mangareader.service.IStorageService;
import com.mangareader.service.IUserService;
import com.mangareader.service.error.InvalidPasswordException;
import com.mangareader.service.error.InvalidUsernameException;
import com.mangareader.service.error.UsernameAlreadyUsedException;
import com.mangareader.service.util.APIUtil;
import com.mangareader.web.rest.vm.ChangePasswordVM;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    private final IStorageService storageService;

    private final PasswordEncoder passwordEncoder;

    private final String AVATAR_FOLDER = "./image/avatar";

    @Override
    @Transactional
    public User createUser(User user) {
        if (user.getId() != null) {
            throw new BadRequestException("New user can not have an id.");
        }
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            log.error("Username is null or blank.");
            throw new InvalidUsernameException("Username is null.");
        }
        if (user.getPassword() == null) {
            log.error("Password is null.");
            throw new InvalidPasswordException("Password is null.");
        }
        if (userRepository.existsByUsername(user.getUsername().toLowerCase())) {
            log.error("Username {} has been used", user.getUsername());
            throw new UsernameAlreadyUsedException();
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserById(Long id) throws ResourceNotFoundException {
        log.info("Find user by id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User " + id + " does not exist."));
    }

    @Override
    public User getUserById(String id) throws ResourceNotFoundException {
        Long idNum = APIUtil.parseStringToLong(id, "id is not a number exception.");
        return getUserById(idNum);
    }

    @Override
    public User getUserByUsername(String username) throws ResourceNotFoundException {
        log.info("Find user by username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User " + username + " does not exist."));
    }

    @Override
    public User getUserByDisplayName(String displayName) throws ResourceNotFoundException {
        return userRepository.findByDisplayNameIgnoreCase(displayName)
                .orElseThrow(() -> new ResourceNotFoundException("User " + displayName + " does not exist."));
    }

    @Override
    public List<User> getUsersByActivateStatus(Boolean activate) throws ResourceNotFoundException {
        log.info("Find users by activate status {}", activate);
        List<User> result = userRepository.findByActivate(activate);
        if (result.isEmpty()) {
            log.error("User list is empty");
            throw new ResourceNotFoundException("There are no such user in the database.");
        }
        return result;
    }

    @Override
    public List<User> getUsers() throws ResourceNotFoundException {
        log.info("Get all user from database....");
        List<User> result = userRepository.findAll();
        if (result.isEmpty()) {
            log.error("User list is empty");
            throw new ResourceNotFoundException("There are no user in the database.");
        }
        return result;
    }

    @Override
    public Page<User> getAllUsersWithPageable(int page, int size) {
        if (size <= 0) {
            throw new BadRequestException("size must be greater than 0.");
        }
        if (page < 0) {
            throw new BadRequestException("Page must be greater than or equal to 0.");
        }
        Pageable pageOption = PageRequest.of(page, size, Sort.by("displayName"));
        return userRepository.findAll(pageOption);
    }

    @Override
    public Page<User> getAllUsersWithPageable(String page, String size) {

        int sizeNum = APIUtil.parseStringToInteger(size, "Size is not a number exception.");
        int pageNum = APIUtil.parseStringToInteger(page, "Page is not a number exception.");
        return getAllUsersWithPageable(pageNum, sizeNum);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional
    public User changeUserRole(Long id, RoleName roleName) {
        User user = getUserById(id);
        log.info("Change role {} to user {}", roleName, user.getUsername());
        user.setRole(roleName);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User changeDisplayName(String displayName) {
        if (displayName == null || displayName.isBlank()) {
            log.error("Error when retrieve displayName: {}.", displayName);
            throw new BadRequestException("displayName is empty.");
        }
        User user = getCurrentUser();
        if (userRepository.existsByDisplayNameIgnoreCase(displayName)) {
            log.error("Error when save display name {}", displayName);
            throw new DataAlreadyExistsException("Display name " + displayName + " already exists.");
        }
        log.info("Set display name {} for user {}", displayName, user.getId());
        user.setDisplayName(displayName);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateAvatar(MultipartFile file) {

        User user = getCurrentUser();
        String fileName = user.getId().toString();
        fileName = storageService.store(file, AVATAR_FOLDER, fileName);
        String avatarUrl = /*SERVER_NAME + */ "/image/avatar/" + fileName;

        user.setAvatarUrl(avatarUrl);
        return userRepository.save(user);
    }

    @Override
    public Resource getAvatar(String fileName) {
        return storageService.loadAsResource(fileName, AVATAR_FOLDER);
    }

    @Override
    public void deleteUserById(Long id) {
        log.debug("Delete user by id: {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public User getCurrentUser() {
        log.debug("Get current user from Security Context Holder....");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return getUserByUsername(username);
    }

    @Override
    public User addServerNameToAvatarURL(User user, String serverName) {
        if (user != null && user.getAvatarUrl() != null) {
            user.setAvatarUrl(serverName + user.getAvatarUrl());
        }
        return user;
    }

    @Override
    public List<User> addServerNameToAvatarURL(List<User> users, String serverName) {
        if (users != null) {
            users.forEach(
                    user -> {
                        if (user.getAvatarUrl() != null)
                            user.setAvatarUrl(serverName + user.getAvatarUrl());
                    }
            );
        }
        return users;
    }

    @Override
    @Transactional
    public User setRoleToUser(Long userId, RoleName roleName) {
        User user = getUserById(userId);
        user.setRole(roleName);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User changeUserStatus(Long userId, Boolean status) {
        User user = getUserById(userId);
        user.setActivate(status);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User changePassword(ChangePasswordVM vm) {
        User user = getCurrentUser();
        boolean match = passwordEncoder.matches(vm.getOldPassword(), user.getPassword());
        if (!match) {
            throw new BadRequestException("Old password does not match.");
        }
        String updatePassword = passwordEncoder.encode(vm.getNewPassword());
        user.setPassword(updatePassword);
        userRepository.save(user);
        return user;
    }

    @Override
    public boolean isUserLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (!(auth instanceof AnonymousAuthenticationToken) && auth != null && auth.isAuthenticated());
    }
}
