package com.mangareader.web.rest;

import com.mangareader.service.AuthenticationService;
import com.mangareader.web.rest.vm.LoginTokenVM;
import com.mangareader.web.rest.vm.UsernamePasswordVM;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class AuthenticateResource {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<LoginTokenVM> register(@Valid @RequestBody UsernamePasswordVM register) throws URISyntaxException {
        LoginTokenVM token = authenticationService.register(register);
        return ResponseEntity.created(new URI("/auth/register")).body(token);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<LoginTokenVM> authenticate(@Valid @RequestBody UsernamePasswordVM authenticate) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticate));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginTokenVM> getNewAccessToken(
            @RequestHeader("refresh_token") String refreshToken
    ) {
        return new ResponseEntity<>(
                authenticationService.getAccessTokenByRefreshToken(refreshToken),
                HttpStatus.OK
        );
    }
}
