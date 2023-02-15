package com.mangareader.service;

import com.mangareader.domain.Role;
import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.repository.UserRepository;
import com.mangareader.service.error.InvalidPasswordException;
import com.mangareader.service.error.InvalidUsernameException;
import com.mangareader.service.error.UsernameAlreadyUsedException;
import com.mangareader.vm.UsernamePasswordVM;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private final JWTService jwtService;

    private final AuthenticationManager authenticationManager;

    public String register(UsernamePasswordVM request) /*throws Exception */ {

        if (request.getUsername() == null) {
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
        if (userRepository.existsByUsername(request.getUsername().toLowerCase())) {
            log.error("Username {} has been used", request.getUsername());
            throw new UsernameAlreadyUsedException();
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setDisplayName(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.saveUser(user);
        user = userService.addRoleToUser(user.getUsername(), "admin");
        var jwtToken = jwtService.generateToken(user);
        return jwtToken;
    }

    public String authenticate(UsernamePasswordVM request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        Map<String, Object> extraClaim = new HashMap<>();
        if (authentication != null) {
            User user = userService.getUser(request.getUsername());

            /*List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());*/

            List<String> roles = user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toList());
            extraClaim.put("Roles", roles);
        }

        User user = userRepository.findByUsername(request.getUsername());
        var jwtToken = jwtService.generateToken(extraClaim, user);
        return jwtToken;
    }

}
