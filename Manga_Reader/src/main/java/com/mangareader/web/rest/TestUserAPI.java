package com.mangareader.web.rest;

import com.mangareader.domain.User;
import com.mangareader.service.IUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "authorize")
@SuppressWarnings("unused")
public class TestUserAPI {

    private final IUserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        log.info("Get all user from database");
        return ResponseEntity.ok(userService.getUsers());
    }
}
