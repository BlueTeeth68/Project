package com.mangareader.web.rest;

import com.mangareader.web.rest.vm.Token;
import com.mangareader.service.AuthenticationService;
import com.mangareader.web.rest.vm.UsernamePasswordVM;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticateResource {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Token> register(@Valid @RequestBody UsernamePasswordVM register) throws URISyntaxException {
        Token token = authenticationService.register(register);
        return ResponseEntity.created(new URI("/auth/register")).body(token);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Token> authenticate(@Valid @RequestBody UsernamePasswordVM authenticate) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticate));
    }
}
