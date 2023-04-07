package com.mangareader.main.repository;

import com.mangareader.domain.Author;
import com.mangareader.domain.Manga;
import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.repository.AuthorRepository;
import com.mangareader.repository.MangaRepository;
import com.mangareader.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MangaRepository mangaRepository;

    @BeforeAll
    void setup() {
        User user = new User();
        user.setUsername("SystemAdmin");
        user.setDisplayName("System Admin");
        user.setRole(RoleName.ADMIN);
        user = userRepository.save(user);

        Manga manga = new Manga();
        manga.setName("Manga 1");
        manga.setUser(user);
        manga.setYearOfPublication(2013);

        for (int i = 1; i <= 5; i++) {
            Author author = new Author();
            author.setName("Author " + i);
            author.setUser(user);
            author.setMangas(Set.of(manga));
            authorRepository.save(author);
        }
    }

    @Test
    @DisplayName("Test findByNameContaining case 1")
    void findByNameContaining_should_search_using_like_ignore_case_1() {
        List<Author> authors = authorRepository.findByNameIgnoreCaseContaining("author");
        assertNotNull(authors);
        assertEquals(5, authors.size());
    }

    @Test
    @DisplayName("Test findByNameContaining case 2")
    void findByNameContaining_should_search_using_like_ignore_case_2() {
        List<Author> authors = authorRepository.findByNameIgnoreCaseContaining("author 1");
        assertEquals(1, authors.size());
    }

    @Test
    void findByUserShouldReturnAList() {
        List<Author> authors = authorRepository.findByUserId(1L);
        assertNotNull(authors);
        assertEquals(5, authors.size());
        assertEquals("Author 1", authors.get(0).getName());
    }

}
