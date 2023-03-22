package com.mangareader.web.rest;

import com.mangareader.domain.User;
import com.mangareader.exception.BadRequestException;
import com.mangareader.service.IUserService;
import com.mangareader.service.dto.CommonUserDTO;
import com.mangareader.service.mapper.UserMapper;
import com.mangareader.service.util.APIUtil;
import com.mangareader.web.rest.vm.ChangePasswordVM;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused")
@SecurityRequirement(name = "authorize")
public class AccountResource {

    private final IUserService userService;
    private final UserMapper userMapper;
    private final HttpServletRequest request;

    @GetMapping
    public ResponseEntity<User> getCurrentLoginUser() {
        User user = userService.getCurrentUser();
        user = userService.addServerNameToAvatarURL(user, APIUtil.getServerName(request));
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
            user = userService.getUserById(id);
        }
        //find by username
        else if (id == null && username != null) {
            user = userService.getUserByUsername(username);
        } else {
            throw new BadRequestException("Bad request for id and username value.");
        }
        user = userService.addServerNameToAvatarURL(user, APIUtil.getServerName(request));
        CommonUserDTO result = userMapper.entityToCommonUserDTO(user);

        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @PatchMapping()
    public ResponseEntity<User> changeDisplayName(
            @RequestBody String displayName
    ) {
        String serverName = APIUtil.getServerName(request);
        User result = userService.changeDisplayName(displayName, serverName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/avatar")
    public ResponseEntity<User> changeUserAvatar(
            @RequestParam("file") MultipartFile file
    ) {
        String serverName = APIUtil.getServerName(request);
        User result = userService.updateAvatar(file, serverName);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PatchMapping("/password")
    public ResponseEntity<User> changePassword(
            @Valid @RequestBody ChangePasswordVM vm
    ) {
        return null;
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserById(
    ) {
        User user = userService.getCurrentUser();
        userService.deleteUserById(user.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
