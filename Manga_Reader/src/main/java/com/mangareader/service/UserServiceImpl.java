package com.mangareader.service;

import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.exception.DataAlreadyExistsException;
import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User saveUser(User user) {

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
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional
    public User changeUserRole(Long id, RoleName roleName) {
        User user = getUserById(id);
        log.info("Change role {} to user {}", roleName, user.getUsername());
        user.setRole(roleName.toString());
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
}
