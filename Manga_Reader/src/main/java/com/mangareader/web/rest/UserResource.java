package com.mangareader.web.rest;

import com.mangareader.service.util.APIUtil;
import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.exception.BadRequestException;
import com.mangareader.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
@Slf4j
public class UserResource {

    private final IUserService userService;

    private final HttpServletRequest request;

    @GetMapping
    public ResponseEntity<User> getUserByIdOrUsername(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String username
    ) {
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

        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllAndPaginateUsers(
            @RequestParam(required = false) String limit,
            @RequestParam(required = false) String page
    ) {
        List<User> users;

        if (limit == null || page == null) {
            users = userService.getAllAndPaginateUsers(1000, 0);
//            users = userService.getUsers();
        } else {
            int limitNum = APIUtil.parseStringToInteger(limit, "Limit is not a number exception.");
            int pageNum = APIUtil.parseStringToInteger(page, "Page is not a number exception.");

            int offset = limitNum * (pageNum - 1);
            users = userService.getAllAndPaginateUsers(limitNum, offset);
        }

        String serverName = getServerName();

        users.forEach(
                user -> {
                    if (user.getAvatarUrl() != null) {
                        user.setAvatarUrl(serverName + user.getAvatarUrl());
                    }
                }
        );

        return new ResponseEntity<>(users, HttpStatus.FOUND);
    }

    @PatchMapping("/role")
    public ResponseEntity<User> changeRoleOfUser(
            @RequestParam String id,
            @RequestParam String role
    ) {
        Long idNum = APIUtil.parseStringToLong(id, "id is not number exception");
        RoleName roleName = APIUtil.parseStringToRoleNameEnum(role.toUpperCase(),
                "Role must be 'USER', 'TRANSLATOR' or 'ADMIN'");
        User user = userService.getUserById(idNum);
        user.setRole(roleName);
        user = userService.saveUser(user);

        if (user.getAvatarUrl() != null) {
            user.setAvatarUrl(getServerName() + user.getAvatarUrl());
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping("/active-status")
    public ResponseEntity<User> changeActiveStatus(
            @RequestParam String id,
            @RequestParam String status
    ) {
        Long idNum = APIUtil.parseStringToLong(id, "id is not number exception");
        Boolean activate = APIUtil.parseStringToBoolean(status, "status is not a boolean variable.");
        User user = userService.getUserById(idNum);
        user.setActivate(activate);
        user = userService.saveUser(user);
        if (user.getAvatarUrl() != null) {
            user.setAvatarUrl(getServerName() + user.getAvatarUrl());
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    private String getServerName() {
        String serverName = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        return serverName;
    }

}
