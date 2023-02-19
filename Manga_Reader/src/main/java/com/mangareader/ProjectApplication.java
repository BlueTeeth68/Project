package com.mangareader;

import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.service.UserService;
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
    CommandLineRunner run(UserService userService) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String password = passwordEncoder.encode("0000");

        return args -> {
//            userService.saveRole(new Role(null, "USER"));
//            userService.saveRole(new Role(null, "TRANSLATOR"));
//            userService.saveRole(new Role(null, "ADMIN"));

            /*userService.saveUser(new User(null, "admin", password, null, null, "admin", null, true, null, new HashSet<>()));
            userService.getRoles();

            userService.addRoleToUser("admin", "user");
            userService.addRoleToUser("admin", "admin");*/

            User user = new User();
            user.setUsername("SystemAdmin");
            user.setDisplayName("System Admin");
            user.setPassword(password);

            /*Role roleAdmin = userService.getRoleByName("ADMIN");
            Role roleUser = userService.getRoleByName("USER");
            user.getRoles().add(roleUser);
            user.getRoles().add(roleAdmin);*/
            user.setRole(RoleName.ADMIN.toString());
            userService.saveUser(user);
        };
    }

}
