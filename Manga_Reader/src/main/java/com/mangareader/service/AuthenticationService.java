package com.mangareader.service;

import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.repository.UserRepository;
import com.mangareader.security.jwt.JWTService;
import com.mangareader.service.error.InvalidPasswordException;
import com.mangareader.service.error.InvalidUsernameException;
import com.mangareader.web.rest.vm.LoginTokenVM;
import com.mangareader.web.rest.vm.UsernamePasswordVM;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class AuthenticationService {
    private final UserRepository userRepository;

    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginTokenVM register(UsernamePasswordVM request) /*throws Exception */ {

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
        user = userService.createUser(user);

        String accessToken = jwtService.generateAccessToken(user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());
        LoginTokenVM result = new LoginTokenVM();
        result.setUser(user);
        result.setAccessToken(accessToken);
        result.setRefreshToken(accessToken);
        return result;
    }

    public LoginTokenVM authenticate(UsernamePasswordVM request, String serverName) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userService.getUserByUsername(request.getUsername());
        String accessToken = jwtService.generateAccessToken(user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());
        LoginTokenVM result = new LoginTokenVM();
        if (user.getAvatarUrl() != null) {
            user = userService.addServerNameToAvatarURL(user, serverName);
        }
        result.setUser(user);
        result.setAccessToken(accessToken);
        result.setRefreshToken(refreshToken);
        return result;
    }

    public LoginTokenVM getAccessTokenByRefreshToken(String refreshToken, String serverName) {
        if (jwtService.isRefreshTokenValid(refreshToken)) {
            String userName = jwtService.extractRefreshUserName(refreshToken);
            User user = userService.getUserByUsername(userName);
            String newAccessToken = jwtService.generateAccessToken(userName);
            String newRefreshToken = jwtService.generateRefreshToken(userName);
            LoginTokenVM result = new LoginTokenVM();
            if(user.getAvatarUrl() != null) {
                user = userService.addServerNameToAvatarURL(user, serverName);
            }
            result.setUser(user);
            result.setAccessToken(newAccessToken);
            result.setRefreshToken(newRefreshToken);
            return result;
        } else {
            throw new BadCredentialsException("refresh token is invalid or expired.");
        }
    }
}
