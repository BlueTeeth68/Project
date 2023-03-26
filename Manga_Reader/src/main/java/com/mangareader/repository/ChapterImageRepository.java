package com.mangareader.repository;

import com.mangareader.domain.ChapterImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterImageRepository extends JpaRepository<ChapterImage, Long> {

    void deleteByChapterId(Long chapterId);

}
