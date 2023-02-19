package com.mangareader.rest;

import com.mangareader.rest.vm.Token;
import com.mangareader.service.AuthenticationService;
import com.mangareader.rest.vm.UsernamePasswordVM;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticateResource {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Token> register(@Valid @RequestBody UsernamePasswordVM register) {
        return ResponseEntity.ok(authenticationService.register(register));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Token> authenticate(@Valid @RequestBody UsernamePasswordVM authenticate) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticate));
    }
}
