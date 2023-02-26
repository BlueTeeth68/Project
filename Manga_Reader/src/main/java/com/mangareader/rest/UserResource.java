package com.mangareader.rest;

import com.mangareader.Util.APIUtil;
import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.exception.BadRequestException;
import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class UserResource {

    private final IUserService userService;

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> getUserByIdOrUsername(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String username
    ) throws URISyntaxException, ResourceNotFoundException, UnknownHostException {
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

        if (result.getAvatarUrl() != null) {
            result.setAvatarUrl(getServerName() + result.getAvatarUrl());
        }

        return ResponseEntity
                .created(new URI("/account/user/" + result.getId()))
                .body(result);
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    //if we want to use hasRole() instead, the role must have prefix "ROLE_"
    // or the function will not work
    public ResponseEntity<List<User>> getAllUser()
            throws URISyntaxException, ResourceNotFoundException, UnknownHostException {
        List<User> users = userService.getUsers();

        //need to improve here

        String serverName = getServerName();

        users.forEach(
                user -> {
                    if (user.getAvatarUrl() != null) {
                        user.setAvatarUrl(serverName + user.getAvatarUrl());
                    }
                }
        );

        return ResponseEntity
                .created(new URI("/admin/users"))
                .body(users);
    }

    @PatchMapping("/user/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> changeRoleOfUser(
            @RequestParam String id,
            @RequestParam String role
    ) throws URISyntaxException, UnknownHostException {
        Long idNum = APIUtil.parseStringToLong(id, "id is not number exception");
        RoleName roleName = APIUtil.parseStringToRoleNameEnum(role.toUpperCase(),
                "Role must be 'USER', 'TRANSLATOR' or 'ADMIN'");
        User user = userService.getUserById(idNum);
        user.setRole(roleName.toString());
        user = userService.saveUser(user);

        if (user.getAvatarUrl() != null) {
            user.setAvatarUrl(getServerName() + user.getAvatarUrl());
        }
        return ResponseEntity
                .created(new URI("/admin/user/role"))
                .body(user);
    }

    @PatchMapping("user/active-status")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> changeActiveStatus(
            @RequestParam String id,
            @RequestParam String status
    ) throws URISyntaxException, UnknownHostException {
        Long idNum = APIUtil.parseStringToLong(id, "id is not number exception");
        Boolean activate = APIUtil.parseStringToBoolean(status, "status is not a boolean variable.");
        User user = userService.getUserById(idNum);
        user.setActivate(activate);
        user = userService.saveUser(user);
        if (user.getAvatarUrl() != null) {
            user.setAvatarUrl(getServerName() + user.getAvatarUrl());
        }
        return ResponseEntity
                .created(new URI("/admin/user/active-status"))
                .body(user);
    }

    private String getServerName() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        String serverName = inetAddress.getHostName();
        return serverName;
    }

}
