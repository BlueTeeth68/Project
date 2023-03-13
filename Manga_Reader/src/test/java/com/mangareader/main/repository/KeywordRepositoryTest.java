package com.mangareader.main.repository;

import com.mangareader.domain.Keyword;
import com.mangareader.repository.KeywordRepository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
//@DataJpaTest
public class KeywordRepositoryTest {

    @Autowired
    private KeywordRepository keywordRepository;

    @Test
    @DisplayName("Test findByMangaIdOrderByName case 1")
    void findByMangaIdOrderByName_should_be_return_a_list() {
        List<Keyword> keywords = keywordRepository.findByMangaIdOrderByName(1L);
        assertEquals(2, keywords.size());
        assertEquals("Đảo Hải Tặc", keywords.get(0).getName());
    }

    @Test
    @DisplayName("Test deleteAllKeywordOfManga case 1")
    @Transactional
    void deleteAllKeywordOfManga_must_delete_all_keyword_of_manga() {
//        keywordRepository.deleteAllKeywordOfManga(1L);
        keywordRepository.deleteByMangaId(1L);
        List<Keyword> keywords = keywordRepository.findByMangaIdOrderByName(1L);
        assertEquals(0, keywords.size());
    }

}
