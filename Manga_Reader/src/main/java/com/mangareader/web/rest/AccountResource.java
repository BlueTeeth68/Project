package com.mangareader.web.rest;

import com.mangareader.service.util.APIUtil;
import com.mangareader.domain.User;
import com.mangareader.exception.BadRequestException;
import com.mangareader.service.IUserService;
import com.mangareader.service.dto.CommonUserDTO;
import com.mangareader.service.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Slf4j
public class AccountResource {

    private final IUserService userService;

    private final UserMapper userMapper;

    private final HttpServletRequest request;

    @GetMapping
    public ResponseEntity<User> getCurrentLoginUser() {
        User user = getCurrentUser();
        if (user.getAvatarUrl() != null) {
            user.setAvatarUrl(getServerName() + user.getAvatarUrl());
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<CommonUserDTO> getUserByIdOrUsername(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String username
    ) {
        User user;

        //find by id
        if (id != null && username == null) {
            Long idNum = APIUtil.parseStringToLong(id, "id is not a number exception.");
            user = userService.getUserById(idNum);
        }
        //find by username
        else if (id == null && username != null) {
            user = userService.getUserByUsername(username);
        } else {
            throw new BadRequestException("Bad request for id and username value.");
        }

        CommonUserDTO result = userMapper.entityToCommonUserDTO(user);

        if (result.getAvatarUrl() != null) {
            result.setAvatarUrl(getServerName() + result.getAvatarUrl());
        }
        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @PatchMapping()
    public ResponseEntity<User> changeDisplayName(
            @RequestBody String displayName
    ) {
        User user = getCurrentUser();

        if (displayName == null || displayName.isBlank()) {
            log.error("Error when retrieve displayName: {}.", displayName);
            throw new BadRequestException("displayName is empty.");
        }

        User result = userService.changeDisplayName(user.getId(), displayName);

        if (result.getAvatarUrl() != null) {
            result.setAvatarUrl(getServerName() + result.getAvatarUrl());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/avatar")
    public ResponseEntity<User> changeUserAvatar(
            @RequestParam("file") MultipartFile file
    ) {
        User user = getCurrentUser();

        User result = userService.updateAvatar(user.getId(), file);

        if (result.getAvatarUrl() != null) {
            result.setAvatarUrl(getServerName() + result.getAvatarUrl());
        }

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserById(
    ) {
        User user = getCurrentUser();
        userService.deleteUserById(user.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private User getCurrentUser() {
        log.debug("Get current user from Security Context Holder....");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.getUserByUsername(username);
        return user;
    }

    private String getServerName() {
        String serverName = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        return serverName;
    }

}
