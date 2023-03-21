package com.mangareader.web.rest;

import com.mangareader.domain.User;
import com.mangareader.exception.BadRequestException;
import com.mangareader.service.IUserService;
import com.mangareader.service.dto.PagingReturnDTO;
import com.mangareader.service.util.APIUtil;
import com.mangareader.web.rest.vm.ChangeUserRoleVM;
import com.mangareader.web.rest.vm.ChangeUserStatusVM;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused")
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
            result = userService.getUserById(id);
        }
        //find by username
        else if (id == null && username != null) {
            result = userService.getUserByUsername(username);
        } else {
            throw new BadRequestException("Bad request for id and username value.");
        }

        String serverName = APIUtil.getServerName(request);
        result = userService.addServerNameToAvatarURL(result, serverName);

        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

//    @GetMapping("/list")
//    public ResponseEntity<List<User>> getAllAndPaginateUsers(
//            @RequestParam(required = false, defaultValue = "100") String limit,
//            @RequestParam(required = false, defaultValue = "1") String page
//    ) {
//        List<User> users = userService.getAllAndPaginateUsers(limit, page);
//        String serverName = APIUtil.getServerName(request);
//        users = userService.addServerNameToAvatarURL(users, serverName);
//        return new ResponseEntity<>(users, HttpStatus.FOUND);
//    }

    @GetMapping("/list")
    public ResponseEntity<PagingReturnDTO<User>> getAllAndPaginateUsers(
            @RequestParam(required = false, defaultValue = "100") String limit,
            @RequestParam(required = false, defaultValue = "1") String page
    ) {
        Page<User> users = userService.getAllUsersWithPageable(page, limit);
        String serverName = APIUtil.getServerName(request);

        /*Page<User> usersModify = users.map(
                user -> userService.addServerNameToAvatarURL(user, serverName));*/

        PagingReturnDTO<User> result = new PagingReturnDTO<>();
        result.setContent(userService.addServerNameToAvatarURL(users.getContent(), serverName));
        result.setTotalElements(users.getTotalElements());
        result.setTotalPages(users.getTotalPages());
        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @PatchMapping("/role")
    public ResponseEntity<User> changeRoleOfUser(
            @RequestBody ChangeUserRoleVM vm
    ) {
        String serverName = APIUtil.getServerName(request);
        User user = userService.setRoleToUser(vm.getId(), vm.getRoleName(), serverName);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping("/active-status")
    public ResponseEntity<User> changeActiveStatus(
            @RequestBody ChangeUserStatusVM vm
    ) {
        String serverName = APIUtil.getServerName(request);
        User user = userService.changeUserStatus(vm.getId(), vm.getStatus(), serverName);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
