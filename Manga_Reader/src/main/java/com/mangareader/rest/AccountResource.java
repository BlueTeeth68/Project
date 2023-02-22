package com.mangareader.rest;

import com.mangareader.Util.APIUtil;
import com.mangareader.domain.User;
import com.mangareader.exception.BadRequestException;
import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> getCurrentUser() throws URISyntaxException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.getUserByUsername(username);
        return ResponseEntity
                .created(new URI("/account/" + username))
                .body(user);
    }

    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> getUserById(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String username
    ) throws URISyntaxException, ResourceNotFoundException {
        User result;

        //find by id
        if (id != null && username == null) {
            Long idNum = APIUtil.parseStringToLong(id, "id is not a number exception.");
            result = userService.getUserById(idNum);
        }
        //find by username
        else if (id == null && username != null) {
            result = userService.getUserByUsername(username);
        } else {
            throw new BadRequestException("Bad request for id and username value.");
        }

        return ResponseEntity
                .created(new URI("/account/user/" + result.getId()))
                .body(result);
    }

    @PatchMapping()
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public ResponseEntity<User> changeDisplayName(
            @RequestParam String id,
            @RequestParam String displayName
    ) throws URISyntaxException {
        Long idNum = APIUtil.parseStringToLong(id, "id is not a number exception.");
        if (displayName == null || displayName.isBlank()) {
            log.error("Error when retrieve displayName: {}.", displayName);
            throw new BadRequestException("displayName is empty.");
        }
        User result = userService.changeDisplayName(idNum, displayName);

        return ResponseEntity
                .created(new URI("/account"))
                .body(result);
    }

}
