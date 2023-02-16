package com.mangareader.rest;

import com.mangareader.domain.User;
import com.mangareader.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserResource {

    private final UserRepository userRepository;

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable(value = "id", required = false) Long id
    ) throws URISyntaxException {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException();
        }

        User result = userRepository.findById(id).orElse(null);
        return ResponseEntity
                .created(new URI("/api/user/" + result.getId()))
                .body(result);
    }
}
