package com.mangareader.main.repository;

import com.mangareader.domain.*;
import com.mangareader.repository.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MangaRepositoryTest {

    @Autowired
    private MangaRepository mangaRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private KeywordRepository keywordRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @BeforeAll
    void setup() {
        User user = new User();
        user.setUsername("SystemAdmin");
        user.setDisplayName("System Admin");
        user.setRole(RoleName.ADMIN);
        user = userRepository.save(user);

        for (int i = 1; i <= 5; i++) {
            Manga manga = new Manga();
            manga.setName("Manga " + i);
            manga.setUser(user);
            manga.setYearOfPublication(2013);
            mangaRepository.save(manga);
        }

        for (int i = 1; i <= 5; i++) {
            Genre genre = new Genre();
            genre.setName("Genre " + i);
            genreRepository.save(genre);
        }
        //add genre to manga
        for (int i = 1; i <= 2; i++) {
            Manga manga = mangaRepository.findById((long) i).orElse(null);
            manga.setGenres(Set.of(
                    genreRepository.findById(1L).orElse(new Genre()),
                    genreRepository.findById(2L).orElse(new Genre())
            ));
            mangaRepository.save(manga);
        }

        for (int i = 3; i <= 5; i++) {
            Manga manga = mangaRepository.findById((long) i).orElse(null);
            manga.setGenres(Set.of(
                    genreRepository.findById(3L).orElse(new Genre()),
                    genreRepository.findById(4L).orElse(new Genre()),
                    genreRepository.findById(5L).orElse(new Genre())
            ));
            mangaRepository.save(manga);
        }
        //add genre to manga

        Author author = new Author();
        author.setUser(user);
        author.setName("Author 1");
        author = authorRepository.save(author);

        //add author to manga
        for (int i = 1; i <= 2; i++) {
            Manga manga = mangaRepository.findById((long) i).orElse(null);
            manga.setAuthors(Set.of(author));
            mangaRepository.save(manga);
        }

        Manga manga = mangaRepository.findById(1L).orElse(null);
        Keyword keyword = new Keyword();
        keyword.setName("OP");
        keyword.setManga(manga);
        keywordRepository.save(keyword);
    }

    @Test
    @DisplayName("Test findAllByOrderByLatestUpdateDesc case 1")
    void findAllByOrderByLatestUpdateDesc_should_return_a_list() {
        List<Manga> mangas = mangaRepository.findAllByOrderByLatestUpdateDesc();
        assertEquals(5, mangas.size());
        assertEquals("Manga 5", mangas.get(0).getName());
    }

    @Test
    @DisplayName("Test findAllByOrderByLatestUpdateDesc case 2")
    void findAllByOrderByLatestUpdateDesc_should_return_limit_record_sort_by_latest_update_1() {
        Pageable pageOption = PageRequest.of(0, 2);
        Page<Manga> mangas = mangaRepository.findAllByOrderByLatestUpdateDesc(pageOption);
        assertEquals(2, mangas.getContent().size());
        assertEquals("Manga 5", mangas.getContent().get(0).getName());
    }

    @Test
    @DisplayName("Test findAllByOrderByLatestUpdateDesc case 3")
    void findAllByOrderByLatestUpdateDesc_should_return_limit_record_sort_by_latest_update_2() {
        Pageable pageOption = PageRequest.of(1, 3);
        Page<Manga> mangas = mangaRepository.findAllByOrderByLatestUpdateDesc(pageOption);
        assertEquals(2, mangas.getContent().size());
        assertEquals("Manga 2", mangas.getContent().get(0).getName());
    }

    @Test
    @DisplayName("Test findPageableMangaByNameOrKeyword case 1")
    void findPageableMangaByNameOrKeywordShouldReturnAList1() {
        Pageable pageOption = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<Manga> mangas = mangaRepository.findPageableMangaByNameOrKeyword("op", pageOption);
        assertNotNull(mangas);
        assertEquals(1, mangas.getContent().size());
        assertEquals("Manga 1", mangas.getContent().get(0).getName());
    }

    @Test
    @DisplayName("Test findPageableMangaByGenreId case 1")
    void findPageableMangaByGenreIdShouldReturnEmptyPage() {
        Pageable pageOption = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<Manga> mangas = mangaRepository.findPageableMangaByGenreId(1L, pageOption);
        assertNotNull(mangas);
        assertEquals(2, mangas.getContent().size());
    }

    @Test
    @DisplayName("Test findPageableMangaByAuthorId case 1")
    void findPageableMangaByAuthorIdShouldReturnEmptyPage() {
        Pageable pageOption = PageRequest.of(0, 4, Sort.by("name").ascending());
        Page<Manga> mangas = mangaRepository.findPageableMangaByAuthorId(1L, pageOption);
        assertNotNull(mangas);
        assertEquals(2, mangas.getContent().size());
    }

    //need to test later
    @Test
    @DisplayName("Test findPageableSuggestManga case 1")
    void findPageableSuggestMangaShouldNotReturnNull() {
        Pageable pageOption = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<Manga> mangas = mangaRepository.findPageableSuggestManga(pageOption);
        assertNotNull(mangas);
        assertEquals(5, mangas.getContent().size());
    }

}
