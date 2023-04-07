package com.mangareader.main.repository;

import com.mangareader.domain.Keyword;
import com.mangareader.domain.Manga;
import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.repository.KeywordRepository;
import com.mangareader.repository.MangaRepository;
import com.mangareader.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KeywordRepositoryTest {

    @Autowired
    private KeywordRepository keywordRepository;
    @Autowired
    private MangaRepository mangaRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void init(){
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

        Manga manga = mangaRepository.findById(1L).orElse(null);
        for (int i = 1; i <= 5; i++) {
            Keyword keyword = new Keyword();
            keyword.setName("Keyword " + i);
            keyword.setManga(manga);
            keywordRepository.save(keyword);
        }
    }

    @Test
    @DisplayName("Test findByMangaIdOrderByName case 1")
    void findByMangaIdOrderByName_should_be_return_a_list() {
        List<Keyword> keywords = keywordRepository.findByMangaIdOrderByName(1L);
        assertEquals(5, keywords.size());
        assertEquals("Keyword 1", keywords.get(0).getName());
    }

    @Test
    @DisplayName("Test deleteByMangaId case 1")
    @Transactional
    void deleteByMangaId_must_delete_all_keyword_of_manga() {
        keywordRepository.deleteByMangaId(1L);
        List<Keyword> keywords = keywordRepository.findByMangaIdOrderByName(1L);
        assertEquals(0, keywords.size());
    }

}
