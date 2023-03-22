package com.mangareader.main.repository;

import com.mangareader.domain.User;
import com.mangareader.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Test findAllUserWithPageable case 1")
    void findAllUserWithPageable_should_return_a_limit_set() {
        Pageable pageOption = PageRequest.of(0, 2);
        Page<User> users = userRepository.findAllUserWithPageable(pageOption);
        assertEquals(2, users.getContent().size());
        assertEquals("Test Translator", users.getContent().get(1).getDisplayName());
    }

}
