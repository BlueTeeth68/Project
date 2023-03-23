package com.mangareader.main.repository;

import com.mangareader.domain.User;
import com.mangareader.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Test findAllUserWithPageable case 1")
    void findAllUserWithPageable_should_return_a_limit_set() {
        Pageable pageOption = PageRequest.of(0, 2);
        Page<User> users = userRepository.findAll(pageOption);
        assertEquals(2, users.getContent().size());
        assertEquals("Test Translator 1", users.getContent().get(1).getDisplayName());
    }

    @Test
    void findByUsernameShouldReturnUser() {
        User user = userRepository.findByUsername("testtranslator1").orElse(null);
        assertNotNull(user);
        assertEquals("TestTranslator1", user.getUsername());
    }

    @Test
    void findByUsernameShouldReturnNull() {
        User user = userRepository.findByUsername(null).orElse(null);
        assertNull(user);
    }

    @Test
    void existsByUsernameShouldReturnFalse1() {
        boolean result = userRepository.existsByUsername(null);
        assertFalse(result);
    }

    @Test
    void existsByUsernameShouldReturnFalse2() {
        boolean result = userRepository.existsByUsername("    ");
        assertFalse(result);
    }

    @Test
    void existsByUsernameShouldReturnTrue() {
        boolean result = userRepository.existsByUsername("testtranslator1");
        assertTrue(result);
    }

    @Test
    void findByActivateShouldReturnEmptyList1() {
        List<User> users = userRepository.findByActivate(null);
        assertNotNull(users);
        assertEquals(0, users.size());
    }

    @Test
    void findByActivateShouldReturnListUser() {
        List<User> users = userRepository.findByActivate(Boolean.TRUE);
        assertNotNull(users);
        assertEquals(14, users.size());
    }

    @Test
    void existsByDisplayNameShouldReturnFalse1() {
        boolean result = userRepository.existsByDisplayName(null);
        assertFalse(result);
    }

    @Test
    void existsByDisplayNameShouldReturnTrue() {
        boolean result = userRepository.existsByDisplayName("common user");
        assertTrue(result);
    }

    @Test
    void existsByDisplayNameShouldReturnFalse2() {
        boolean result = userRepository.existsByDisplayName("common");
        assertFalse(result);
    }

    @Test
    void findAllShouldReturnLimitUser() {
        Pageable pageOption = PageRequest.of(0, 4, Sort.by("displayName").ascending());
        Page<User> users = userRepository.findAll(pageOption);
        assertNotNull(users);
        assertEquals(4, users.getContent().size());
        assertEquals("Common User", users.getContent().get(0).getDisplayName());
    }

    @Test
    void findAllShouldReturnEmptyContent1() {
        Assertions.assertThatThrownBy(
                        () -> PageRequest.of(-1, 4, Sort.by("displayName").ascending())
                )
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findAllShouldReturnEmptyContent2() {
        Pageable pageOption = PageRequest.of(3, 10, Sort.by("displayName").ascending());
        Page<User> users = userRepository.findAll(pageOption);
        assertNotNull(users);
        assertEquals(0, users.getContent().size());
    }

}
