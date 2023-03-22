package com.mangareader.main.repository;

import com.mangareader.domain.Genre;
import com.mangareader.repository.GenreRepository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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

}
