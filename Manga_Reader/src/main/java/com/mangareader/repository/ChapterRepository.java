package com.mangareader.repository;

import com.mangareader.domain.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    List<Chapter> findByMangaIdOrderByIdDesc(Long mangaId);

    Boolean existsByMangaIdAndChapterNumber(Long mangaId, Float chapterNumber);

}
