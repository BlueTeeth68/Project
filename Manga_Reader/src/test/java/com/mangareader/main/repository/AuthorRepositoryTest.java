package com.mangareader.main.repository;

import com.mangareader.domain.Author;
import com.mangareader.repository.AuthorRepository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
//@DataJpaTest
public class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    @DisplayName("Test findByNameContaining case 1")
    void findByNameContaining_should_search_using_like_ignore_case_1() {
        List<Author> authors = authorRepository.findByNameContaining("aut");
        assertEquals(3, authors.size());
    }

    @Test
    @DisplayName("Test findByNameContaining case 2")
    void findByNameContaining_should_search_using_like_ignore_case_2() {
        List<Author> authors = authorRepository.findByNameContaining("author 1");
        assertEquals(1, authors.size());
    }

    @Test
    @DisplayName("Test findLimitAuthor case 1")
    void findLimitAuthor_should_return_limit_record_1() {
        List<Author> authors = authorRepository.findLimitAuthor(2, 0);
        assertEquals(2, authors.size());
        assertEquals(1L, authors.get(0).getId());
    }

    @Test
    @DisplayName("Test findLimitAuthor case 2")
    void findLimitAuthor_should_return_limit_record_2() {
        List<Author> authors = authorRepository.findLimitAuthor(2, 2);
        assertEquals(1, authors.size());
        assertEquals(3L, authors.get(0).getId());
    }

}
