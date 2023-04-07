package com.mangareader.main.repository;

import com.mangareader.domain.Keyword;
import com.mangareader.repository.KeywordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
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
    @DisplayName("Test deleteByMangaId case 1")
    @Transactional
    void deleteByMangaId_must_delete_all_keyword_of_manga() {
        keywordRepository.deleteByMangaId(1L);
        List<Keyword> keywords = keywordRepository.findByMangaIdOrderByName(1L);
        assertEquals(0, keywords.size());
    }

}
