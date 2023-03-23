package com.mangareader.main.repository;

import com.mangareader.domain.Author;
import com.mangareader.repository.AuthorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
//@DataJpaTest
public class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    @DisplayName("Test findByNameContaining case 1")
    void findByNameContaining_should_search_using_like_ignore_case_1() {
        List<Author> authors = authorRepository.findByNameContaining("author");
        assertEquals(6, authors.size());
    }

    @Test
    @DisplayName("Test findByNameContaining case 2")
    void findByNameContaining_should_search_using_like_ignore_case_2() {
        List<Author> authors = authorRepository.findByNameContaining("author 1");
        assertEquals(1, authors.size());
    }

    @Test
    void findByUserShouldReturnAList() {
        List<Author> authors = authorRepository.findByUserId(2L);
        assertNotNull(authors);
        assertEquals(3, authors.size());
        assertEquals("Author 1", authors.get(0).getName());
    }

}
