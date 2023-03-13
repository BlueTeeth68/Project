package com.mangareader.main.repository;

import com.mangareader.domain.Manga;
import com.mangareader.repository.MangaRepository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
//@DataJpaTest
public class MangaRepositoryTest {

    @Autowired
    private MangaRepository mangaRepository;

    @Test
    @DisplayName("Test findAllByOrderByLatestUpdateDesc case 1")
    void findAllByOrderByLatestUpdateDesc_should_return_a_list() {
        List<Manga> mangas = mangaRepository.findAllByOrderByLatestUpdateDesc();
        assertEquals(3, mangas.size());
        assertEquals("My Hero Academia", mangas.get(0).getName());
    }

    @Test
    @DisplayName("Test findAllAndPaginateOrderByLatestUpdate case 1")
    void findAllAndPaginateOrderByLatestUpdate_should_return_limit_record_sort_by_latest_update_1() {
        List<Manga> mangas = mangaRepository.findAllAndPaginateOrderByLatestUpdate(2, 0);
        assertEquals(2, mangas.size());
        assertEquals("My Hero Academia", mangas.get(0).getName());
    }

    @Test
    @DisplayName("Test findAllAndPaginateOrderByLatestUpdate case 2")
    void findAllAndPaginateOrderByLatestUpdate_should_return_limit_record_sort_by_latest_update_2() {
        List<Manga> mangas = mangaRepository.findAllAndPaginateOrderByLatestUpdate(2, 2);
        assertEquals(1, mangas.size());
        assertEquals("One piece", mangas.get(0).getName());
    }

    @Test
    @DisplayName("Test findByNameContainingOrderByName case 1")
    void findByNameContainingOrderByName_should_search_by_name_using_like_order_by_name() {
        List<Manga> mangas = mangaRepository.findByNameContainingOrderByName("o");
        assertEquals(3, mangas.size());
        assertEquals("Naruto", mangas.get(1).getName());
    }
}
