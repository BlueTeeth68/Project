package com.mangareader.main.repository;

import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
//@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    //Can not invoke bean PasswordEncoder with @DataJpaTest.
    // Use @SpringBootTest instead
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @BeforeAll
    void setup() {
//        String password = passwordEncoder.encode("0000");

        User user = new User();
        user.setUsername("SystemAdmin");
        user.setDisplayName("System Admin");
//        user.setPassword(password);
        user.setRole(RoleName.ADMIN);
        userRepository.save(user);

        for (int i = 1; i <= 5; i++) {
            User temp = new User();
            temp.setUsername("translator" + i);
            temp.setDisplayName("Translator " + i);
//            user.setPassword(password);
            temp.setRole(RoleName.TRANSLATOR);
            userRepository.save(temp);
        }

        for (int i = 1; i <= 5; i++) {
            User temp = new User();
            temp.setUsername("user" + i);
            temp.setDisplayName("User " + i);
//            user.setPassword(password);
            temp.setRole(RoleName.USER);
            userRepository.save(temp);
        }
    }

    @Test
    @DisplayName("Test findAllUserWithPageable case 1")
    void findAllUserWithPageable_should_return_a_limit_set() {
        Pageable pageOption = PageRequest.of(0, 4);
        Page<User> users = userRepository.findAll(pageOption);
        assertEquals(4, users.getContent().size());
        assertEquals("System Admin", users.getContent().get(0).getDisplayName());
    }

    @Test
    void findByUsernameShouldReturnUser() {
        User user = userRepository.findByUsername("translator1").orElse(null);
        assertNotNull(user);
        assertEquals("Translator 1", user.getDisplayName());
    }

    @Test
    void findByUsernameShouldReturnNull() {
        User user = userRepository.findByUsername("blueteeth").orElse(null);
        assertNull(user);
    }

    @Test
    void existsByUsernameShouldReturnFalseCase1() {
        boolean result = userRepository.existsByUsername("blueteeth");
        assertFalse(result);
    }

    @Test
    void existsByUsernameShouldReturnFalseCase2() {
        boolean result = userRepository.existsByUsername("    ");
        assertFalse(result);
    }

    @Test
    void existsByUsernameShouldReturnTrue() {
        boolean result = userRepository.existsByUsername("translator1");
        assertTrue(result);
    }

    @Test
    void findByActivateShouldReturnEmptyListCase1() {
        List<User> users = userRepository.findByActivate(null);
        assertNotNull(users);
        assertEquals(0, users.size());
    }

    @Test
    void findByActivateShouldReturnListUser() {
        List<User> users = userRepository.findByActivate(Boolean.TRUE);
        assertNotNull(users);
        assertEquals(11, users.size());
    }

    @Test
    void existsByDisplayNameShouldReturnFalseCase1() {
        boolean result = userRepository.existsByDisplayNameIgnoreCase(null);
        assertFalse(result);
    }

    @Test
    void existsByDisplayNameShouldReturnTrue() {
        boolean result = userRepository.existsByDisplayNameIgnoreCase("system admin");
        assertTrue(result);
    }

    @Test
    void existsByDisplayNameShouldReturnFalseCase2() {
        boolean result = userRepository.existsByDisplayNameIgnoreCase("Blue Teeth");
        assertFalse(result);
    }

    @Test
    void findAllShouldReturnLimitUser() {
        Pageable pageOption = PageRequest.of(0, 4, Sort.by("displayName").ascending());
        Page<User> users = userRepository.findAll(pageOption);
        assertNotNull(users);
        assertEquals(4, users.getContent().size());
        assertEquals("System Admin", users.getContent().get(0).getDisplayName());
    }

    @Test
    void findAllShouldReturnEmptyContent() {
        Pageable pageOption = PageRequest.of(3, 10, Sort.by("displayName").ascending());
        Page<User> users = userRepository.findAll(pageOption);
        assertNotNull(users);
        assertEquals(0, users.getContent().size());
    }

}
