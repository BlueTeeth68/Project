package com.mangareader.rest;

import com.mangareader.domain.User;
import com.mangareader.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserResource {

    private final UserRepository userRepository;

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable(value = "id", required = false) Long id
    ) throws URISyntaxException {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException();
        }
        log.info("Find user by id: {}", id);
        User result = userRepository.findById(id).orElse(null);
        return ResponseEntity
                .created(new URI("/api/users/" + result.getId()))
                .body(result);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUser() throws URISyntaxException {
        log.info("Get all user from database....");
        List<User> users = userRepository.findAll();
        return ResponseEntity
                .created(new URI("/api/users"))
                .body(users);
    }

}
