package com.mangareader.rest;

import com.mangareader.domain.User;
import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class UserResource {

    private final UserService userService;

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> getUserById(
            @PathVariable(value = "id") Long id
    ) throws URISyntaxException, ResourceNotFoundException {
        User result = userService.getUserById(id);
        return ResponseEntity
                .created(new URI("/api/admin/users/" + result.getId()))
                .body(result);
    }

    @GetMapping("/users/username/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> getUserByUsername(
            @PathVariable(value = "username") String username
    ) throws URISyntaxException, ResourceNotFoundException {
        User result = userService.getUserByUsername(username);
        return ResponseEntity
                .created(new URI("/api/admin/users/username/" + result.getUsername()))
                .body(result);
    }

    @GetMapping("users/activate/{activate}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<User>> getUsersByActiveStatus(
            @PathVariable(value = "activate") Boolean activate
    ) throws URISyntaxException, ResourceNotFoundException {
        List<User> result = userService.getUsersByActivateStatus(activate);
        return ResponseEntity
                .created(new URI("/api/users/active/" + activate))
                .body(result);
    }


    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    //if we want to use hasRole() instead, the role must have prefix "ROLE_"
    // or the function will not work
    public ResponseEntity<List<User>> getAllUser() throws URISyntaxException, ResourceNotFoundException {
        List<User> users = userService.getUsers();
        return ResponseEntity
                .created(new URI("/api/users"))
                .body(users);
    }

//    public ResponseEntity<User> addRoleToUser

}
