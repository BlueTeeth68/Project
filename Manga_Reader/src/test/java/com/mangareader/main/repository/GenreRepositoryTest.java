package com.mangareader.main.repository;

import com.mangareader.domain.Genre;
import com.mangareader.repository.GenreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

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
        Genre genre = genreRepository.findByName("shounen").orElse(null);
        assertNotNull(genre);
        assertEquals("Shounen", genre.getName());
    }

    @Test
    void existsByNameShouldReturnTrue() {
        boolean result = genreRepository.existsByName("shounen");
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
        List<Genre> genres = genreRepository.findByNameContainingOrderByNameAsc("O");
        assertNotNull(genres);
        assertEquals(4, genres.size());
        assertEquals("Romance", genres.get(2).getName());
    }

}
