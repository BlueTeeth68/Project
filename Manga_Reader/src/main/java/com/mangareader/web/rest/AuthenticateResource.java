package com.mangareader.web.rest;

import com.mangareader.service.AuthenticationService;
import com.mangareader.web.rest.vm.LoginTokenVM;
import com.mangareader.web.rest.vm.UsernamePasswordVM;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@SuppressWarnings("unused")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Query successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request for input parameters", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized, missing or invalid JWT", content = @Content),
        @ApiResponse(responseCode = "403", description = "Access denied, do not have permission to access this resource", content = @Content),
})
public class AuthenticateResource {

    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Register account",
            description = "User can register a new account by provide username and password.", tags = "Authenticate")
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginTokenVM> register(@Valid @RequestBody UsernamePasswordVM register) throws URISyntaxException {
        LoginTokenVM token = authenticationService.register(register);
        return ResponseEntity.created(new URI("/auth/register")).body(token);
    }

    @Operation(
            summary = "Login",
            description = "User can log in to their account by using username and password.", tags = "Authenticate")
    @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginTokenVM> authenticate(@Valid @RequestBody UsernamePasswordVM authenticate) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticate));
    }

    @Operation(
            summary = "Get access token",
            description = "User can get access token by providing refresh token.", tags = "Authenticate")
    @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginTokenVM> getNewAccessToken(
            @RequestHeader("refresh_token") String refreshToken
    ) {
        return new ResponseEntity<>(
                authenticationService.getAccessTokenByRefreshToken(refreshToken),
                HttpStatus.OK
        );
    }
}
