package com.mangareader.rest;

import com.mangareader.Util.APIUtil;
import com.mangareader.domain.User;
import com.mangareader.exception.BadRequestException;
import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.service.IUserService;
import com.mangareader.service.dto.CommonUserDTO;
import com.mangareader.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Slf4j
public class AccountResource {

    private final IUserService userService;

    private final UserMapper userMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)

    public ResponseEntity<User> getCurrentLoginUser() throws URISyntaxException {
        User user = getCurrentUser();
        return ResponseEntity
                .created(new URI("/account/" + user.getDisplayName()))
                .body(user);
    }

    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CommonUserDTO> getUserByIdOrUsername(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String username
    ) throws URISyntaxException, ResourceNotFoundException {
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
        return ResponseEntity
                .created(new URI("/account/user/" + result.getId()))
                .body(result);
    }

    @PatchMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> changeDisplayName(
            @RequestParam String displayName
    ) throws URISyntaxException {

        User user = getCurrentUser();

        if (displayName == null || displayName.isBlank()) {
            log.error("Error when retrieve displayName: {}.", displayName);
            throw new BadRequestException("displayName is empty.");
        }

        User result = userService.changeDisplayName(user.getId(), displayName);

        return ResponseEntity
                .created(new URI("/account"))
                .body(result);
    }

//    @PostMapping("/avatar")
//    public ResponseEntity<User> changeUserAvatar(
//            @RequestParam
//    ) {
//
//    }


    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.getUserByUsername(username);

        return user;
    }

}
