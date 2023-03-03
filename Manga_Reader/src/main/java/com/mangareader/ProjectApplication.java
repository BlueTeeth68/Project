package com.mangareader;

import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.service.IUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @Bean
    CommandLineRunner run(IUserService userService) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String password = passwordEncoder.encode("0000");

        return args -> {

            User user = new User();
            user.setUsername("SystemAdmin");
            user.setDisplayName("System Admin");
            user.setPassword(password);
            user.setRole(RoleName.ADMIN);
            userService.saveUser(user);

            User commonUser = new User();
            commonUser.setUsername("User");
            commonUser.setDisplayName("Common User");
            commonUser.setPassword(password);
            commonUser.setRole(RoleName.USER);
            userService.saveUser(commonUser);

            for (int i = 0; i < 20; i++) {
                commonUser = new User();
                commonUser.setUsername("User" + (i + 1));
                commonUser.setDisplayName("Common User " + (i + 1));
                commonUser.setPassword(password);
                commonUser.setRole(RoleName.USER);
                userService.saveUser(commonUser);
            }
        };
    }

}
