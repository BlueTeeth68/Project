package com.mangareader.service;

import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.web.rest.vm.Token;
import com.mangareader.security.jwt.JWTService;
import com.mangareader.service.error.InvalidPasswordException;
import com.mangareader.service.error.InvalidUsernameException;
import com.mangareader.service.error.UsernameAlreadyUsedException;
import com.mangareader.web.rest.vm.UsernamePasswordVM;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public Token register(UsernamePasswordVM request) /*throws Exception */ {

        if (request.getUsername() == null || request.getUsername().isBlank()) {
            log.error("Username is null.");
            throw new InvalidUsernameException("Username is null.");
        }
        if (request.getPassword() == null) {
            log.error("Password is null.");
            throw new InvalidPasswordException("Password is null.");
        }
        if (request.getPassword().length() < 4) {
            log.error("Password must have at least 4 character.");
            throw new InvalidPasswordException("Password must have at least 4 character.");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setDisplayName(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(RoleName.USER);
        userService.saveUser(user);

        String jwtToken = jwtService.generateToken(user);
        return new Token(jwtToken);
    }

    public Token authenticate(UsernamePasswordVM request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userService.getUserByUsername(request.getUsername());
        var jwtToken = jwtService.generateToken(user);
        return new Token(jwtToken);
    }

}
