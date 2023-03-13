package com.mangareader.main.repository;

import com.mangareader.domain.User;
import com.mangareader.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
//@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Test findAllAndPaginateUser case 1")
    void findAllAndPaginateUser_should_return_a_limit_set() {
        List<User> users = userRepository.findAllAndPaginateUser(2, 0);
        assertEquals(2, users.size());
        assertEquals("Test Translator", users.get(1).getDisplayName());
    }

}
