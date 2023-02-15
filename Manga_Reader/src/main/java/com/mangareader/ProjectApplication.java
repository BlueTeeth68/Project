package com.mangareader;

import com.mangareader.domain.Role;
import com.mangareader.domain.User;
import com.mangareader.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;

@SpringBootApplication
public class ProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserService userService) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = encoder.encode("0000");

        return args -> {
            userService.saveRole(new Role(null, "user", new HashSet<>()));
            userService.saveRole(new Role(null, "admin", new HashSet<>()));
            userService.saveRole(new Role(null, "translator", new HashSet<>()));

            /*userService.saveUser(new User(null, "admin", password, null, null, "admin", null, true, null, new HashSet<>()));
            userService.getRoles();

            userService.addRoleToUser("admin", "user");
            userService.addRoleToUser("admin", "admin");*/
        };
    }

}
