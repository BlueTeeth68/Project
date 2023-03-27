package com.mangareader.repository;

import com.mangareader.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByUserIdAndMangaId(Long userId, Long mangaId);

    void deleteByUserIdAndMangaId(Long userId, Long mangaId);

    List<Bookmark> findByUserIdOrderByIdDesc(Long userId);
}
