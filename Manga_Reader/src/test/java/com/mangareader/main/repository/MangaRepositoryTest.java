package com.mangareader.main.repository;

import com.mangareader.domain.Manga;
import com.mangareader.repository.MangaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
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
    @DisplayName("Test findAllByOrderByLatestUpdateDesc case 1")
    void findAllByOrderByLatestUpdateDesc_should_return_limit_record_sort_by_latest_update_1() {
        Pageable pageOption = PageRequest.of(0, 2);
        Page<Manga> mangas = mangaRepository.findAllByOrderByLatestUpdateDesc(pageOption);
        assertEquals(2, mangas.getContent().size());
        assertEquals("My Hero Academia", mangas.getContent().get(0).getName());
    }

    @Test
    @DisplayName("Test findAllByOrderByLatestUpdateDesc case 2")
    void findAllByOrderByLatestUpdateDesc_should_return_limit_record_sort_by_latest_update_2() {
        Pageable pageOption = PageRequest.of(1, 2);
        Page<Manga> mangas = mangaRepository.findAllByOrderByLatestUpdateDesc(pageOption);
        assertEquals(1, mangas.getContent().size());
        assertEquals("One piece", mangas.getContent().get(0).getName());
    }

    @Test
    @DisplayName("Test findByNameContainingOrderByName case 1")
    void findByNameContainingOrderByName_should_search_by_name_using_like_order_by_name() {
        List<Manga> mangas = mangaRepository.findByNameContainingOrderByName("o");
        assertEquals(3, mangas.size());
        assertEquals("Naruto", mangas.get(1).getName());
    }

    @Test
    @DisplayName("Test findPageableMangaByGenreId case 1")
    void findPageableMangaByGenreIdShouldReturnEmptyPage() {
        Pageable pageOption = PageRequest.of(1, 10, Sort.by("name").ascending());
        Page<Manga> mangas = mangaRepository.findPageableMangaByGenreId(1L, pageOption);
        assertNotNull(mangas);
    }

    @Test
    @DisplayName("Test findPageableMangaByAuthorId case 1")
    void findPageableMangaByAuthorIdShouldReturnEmptyPage() {
        Pageable pageOption = PageRequest.of(1, 10, Sort.by("name").ascending());
        Page<Manga> mangas = mangaRepository.findPageableMangaByAuthorId(1L, pageOption);
        assertNotNull(mangas);
    }

    @Test
    @DisplayName("Test findPageableSuggestManga case 1")
    void findfindPageableSuggestMangaShouldNotReturnNull() {
        Pageable pageOption = PageRequest.of(1, 10, Sort.by("name").ascending());
        Page<Manga> mangas = mangaRepository.findPageableSuggestManga(pageOption);
        assertNotNull(mangas);
    }

    @Test
    @DisplayName("Test findPageableMangaByNameOrKeyword case 1")
    void findfindPageableMangaByNameOrKeywordShouldNotReturnNull() {
        Pageable pageOption = PageRequest.of(1, 10, Sort.by("name").ascending());
        Page<Manga> mangas = mangaRepository.findPageableMangaByNameOrKeyword("abc", pageOption);
        assertNotNull(mangas);
    }
}
