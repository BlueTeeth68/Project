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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    private final IStorageService storageService;

    private final String AVATAR_FOLDER = "./image/avatar";

//    @Value("${server.name}")
//    private String SERVER_NAME;

    @Override
    @Transactional
    public User saveUser(User user) {
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
    public User getUserByUsername(String username) throws ResourceNotFoundException {
        log.info("Find user by username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User " + username + " does not exist."));
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
    public List<User> getAllAndPaginateUsers(int limit, int offset) {
        if (limit <= 0) {
            throw new BadRequestException("limit must be greater than 0.");
        }
        if (offset < 0) {
            throw new BadRequestException("offset must be greater than or equal to 0.");
        }
        List<User> result = userRepository.findAllAndPaginateUser(limit, offset);
        return result;
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
        return saveUser(user);
    }

    @Override
    @Transactional
    public User changeDisplayName(Long id, String displayName) {
        User user = getUserById(id);
        if (userRepository.existsByDisplayName(displayName)) {
            log.error("Error when save display name {}", displayName);
            throw new DataAlreadyExistsException("Display name " + displayName + " already exists.");
        }
        log.info("Set display name {} for user {}", displayName, id);
        user.setDisplayName(displayName);
        user = userRepository.save(user);
        return user;
    }

    @Override
    public User updateAvatar(User user, MultipartFile file) {

        String fileName = user.getId().toString();

        fileName = storageService.store(file, AVATAR_FOLDER, fileName);

        String avatarUrl = /*SERVER_NAME + */ "/image/avatar/" + fileName;

        user.setAvatarUrl(avatarUrl);
        user = saveUser(user);

        return user;
    }

    @Override
    public Resource getAvatar(String fileName) {
        Resource file = storageService.loadAsResource(fileName, AVATAR_FOLDER);
        return file;
    }

    @Override
    public void deleteUserById(Long id) {
        log.debug("Delete user by id: {}", id);
        userRepository.deleteById(id);
    }
}
