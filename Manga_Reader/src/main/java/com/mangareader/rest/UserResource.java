package com.mangareader.rest;

import com.mangareader.Util.APIUtil;
import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.exception.BadRequestException;
import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class UserResource {

    private final UserService userService;


//    @GetMapping("/users/username/{username}")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<User> getUserByUsername(
//            @PathVariable(value = "username") String username
//    ) throws URISyntaxException, ResourceNotFoundException {
//        User result = userService.getUserByUsername(username);
//        return ResponseEntity
//                .created(new URI("/api/admin/users/username/" + result.getUsername()))
//                .body(result);
//    }

//    @GetMapping("users/activate/{activate}")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<List<User>> getUsersByActiveStatus(
//            @PathVariable(value = "activate") Boolean activate
//    ) throws URISyntaxException, ResourceNotFoundException {
//        List<User> result = userService.getUsersByActivateStatus(activate);
//        return ResponseEntity
//                .created(new URI("/api/users/active/" + activate))
//                .body(result);
//    }


    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    //if we want to use hasRole() instead, the role must have prefix "ROLE_"
    // or the function will not work
    public ResponseEntity<List<User>> getAllUser()
            throws URISyntaxException, ResourceNotFoundException {
        List<User> users = userService.getUsers();
        return ResponseEntity
                .created(new URI("/admin/users"))
                .body(users);
    }

    @PatchMapping("/user/change-role")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public ResponseEntity<User> changeRoleOfUser(
            @RequestParam String id,
            @RequestParam String role
    ) throws URISyntaxException {
        Long idNum = APIUtil.parseStringToLong(id, "id is not number exception");
        RoleName roleName = APIUtil.parseStringToRoleNameEnum(role.toUpperCase(),
                "Role must be 'USER', 'TRANSLATOR' or 'ADMIN'");
        User user = userService.getUserById(idNum);
        user.setRole(roleName.toString());
        user = userService.saveUser(user);
        return ResponseEntity
                .created(new URI("/admin/user/change-role"))
                .body(user);
    }

    @PatchMapping("user/change-active-status")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public ResponseEntity<User> changeActiveStatus(
            @RequestParam String id,
            @RequestParam String status
    ) throws URISyntaxException {
        Long idNum = APIUtil.parseStringToLong(id, "id is not number exception");
        Boolean activate = APIUtil.parseStringToBoolean(status, "status is not a boolean variable.");
        User user = userService.getUserById(idNum);
        user.setActivate(activate);
        user = userService.saveUser(user);
        return ResponseEntity
                .created(new URI("/admin/user/change-active-status"))
                .body(user);
    }

}
