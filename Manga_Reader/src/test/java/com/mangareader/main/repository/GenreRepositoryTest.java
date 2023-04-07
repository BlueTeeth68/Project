package com.mangareader.main.repository;

import com.mangareader.domain.Genre;
import com.mangareader.repository.GenreRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @BeforeAll
    void setup() {
        Genre genre;
        genre = new Genre();
        genre.setName("Action");
        genreRepository.save(genre);

        genre = new Genre();
        genre.setName("Shounen");
        genreRepository.save(genre);

        genre = new Genre();
        genre.setName("Romance");
        genreRepository.save(genre);

        genre = new Genre();
        genre.setName("Fiction");
        genreRepository.save(genre);

        genre = new Genre();
        genre.setName("Fantastic");
        genreRepository.save(genre);

        genre = new Genre();
        genre.setName("Scientific");
        genreRepository.save(genre);
    }

    @Test
    @DisplayName("Test findAllByOrderByNameAsc case 1")
    void findAllByOrderByNameAsc_should_be_sort_by_name_1() {
        List<Genre> genres = genreRepository.findAllByOrderByNameAsc();
        assertEquals(6, genres.size());
        assertEquals("Action", genres.get(0).getName());
        assertEquals("Fantastic", genres.get(1).getName());
    }

    @Test
    void findByNameShouldReturnAGenre() {
        Genre genre = genreRepository.findByName("Shounen").orElse(null);
        assertNotNull(genre);
        assertEquals("Shounen", genre.getName());
    }

    @Test
    void existsByNameShouldReturnTrue() {
        boolean result = genreRepository.existsByName("Shounen");
        assertTrue(result);
    }

    @Test
    void existsByNameShouldReturnFalse() {
        boolean result = genreRepository.existsByName(null);
        assertFalse(result);
    }

    @Test
    void findAllByOrderByNameAscShouldReturnAList() {
        List<Genre> genres = genreRepository.findAllByOrderByNameAsc();
        assertNotNull(genres);
        assertEquals(6, genres.size());
        assertEquals("Action", genres.get(0).getName());
    }

    @Test
    void findByNameContainingOrderByNameAsc() {
        List<Genre> genres = genreRepository.findByNameIgnoreCaseContainingOrderByNameAsc("O");
        assertNotNull(genres);
        assertEquals(4, genres.size());
        assertEquals("Romance", genres.get(2).getName());
    }

}
