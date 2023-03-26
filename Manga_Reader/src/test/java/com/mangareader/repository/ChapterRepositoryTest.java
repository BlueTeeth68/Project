package com.mangareader.repository;

import com.mangareader.domain.Chapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ChapterRepositoryTest {

    @Autowired
    private ChapterRepository chapterRepository;

    @Test
    void findByMangaIdOrderByIdDesc() {
        List<Chapter> chapters = chapterRepository.findByMangaIdOrderByIdDesc(1L);
    }
}