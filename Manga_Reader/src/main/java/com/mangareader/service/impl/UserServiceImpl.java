package com.mangareader.service.impl;

import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.exception.BadRequestException;
import com.mangareader.exception.DataAlreadyExistsException;
import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.repository.UserRepository;
import com.mangareader.service.IUserService;
import com.mangareader.service.error.InvalidPasswordException;
import com.mangareader.service.error.InvalidUsernameException;
import com.mangareader.service.error.UsernameAlreadyUsedException;
import com.mangareader.service.util.APIUtil;
import com.mangareader.web.rest.vm.ChangePasswordVM;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.TimeoutException;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final AWSStorageService storageService;
    private final PasswordEncoder passwordEncoder;
    private final String AVATAR_FOLDER = "image/avatar/";

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
    public boolean existsByUsername(String username) {
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
    public User updateAvatar(MultipartFile file) throws TimeoutException {
        User user = getCurrentUser();

        //get old avatarUrl
        String oldUrl = user.getAvatarUrl();
        log.info("Old Url is: {}", oldUrl);

        String url = storageService.uploadImage(file, AVATAR_FOLDER);
        log.info("New url is: {}", url);

        user.setAvatarUrl(url);
        //delete old avatar
        if (oldUrl != null && !oldUrl.equals(url)) {
            String[] tmp = oldUrl.split("image");
            StringBuffer fileName = new StringBuffer();
            for (int i = 1; i < tmp.length; i++) {
                fileName.append("image");
                fileName.append(tmp[i]);
            }
            log.info("File name is: {}", fileName);
            storageService.deleteFile(fileName.toString());
        }
        return userRepository.save(user);
    }

//    @Override
//    public Resource getAvatar(String fileName) {
//        return storageService.loadAsResource(fileName, AVATAR_FOLDER);
//    }

    @Override
    public void deleteUserById(Long id) {
        log.debug("Delete user by id: {}", id);
        User user = getUserById(id);
        //can not delete admin user
        if(user.getRole().name().equals("ADMIN")) {
            throw new BadRequestException("Admin account can not be deleted.");
        }
        userRepository.deleteById(id);
    }

    @Override
    public User getCurrentUser() {
        if(!isUserLogin()) {
            throw new BadCredentialsException("You have not logged in yet.");
        }
        log.debug("Get current user from Security Context Holder....");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return getUserByUsername(username);
    }

    @Override
    @Transactional
    //Test later
    public User setRoleToUser(Long userId, RoleName roleName) {
        User user = getUserById(userId);
        if (user.getRole().name().equals("ADMIN")) {
            throw new BadRequestException("Can not change role of Admin user.");
        }
        user.setRole(roleName);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User changeUserStatus(Long userId, Boolean status) {
        User user = getUserById(userId);
        if (user.getRole().name().equals("ADMIN")) {
            throw new BadRequestException("Can not change status of Admin user.");
        }
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
