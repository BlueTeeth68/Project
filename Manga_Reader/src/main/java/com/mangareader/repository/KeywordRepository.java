package com.mangareader.repository;

import com.mangareader.domain.Keyword;
import com.mangareader.domain.KeywordId;
import com.mangareader.domain.Manga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, KeywordId> {

    List<Keyword> findByMangaIdOrderByName(Long mangaId);

    @Query(value = "SELECT * FROM manga " +
            " WHERE id IN ( SELECT manga_id FROM keyword " +
            " WHERE name LIKE '%' + ?1 + '%' )", nativeQuery = true)
    List<Manga> findMangasByKeyword(String keyword);

    @Query(value = " DELETE FROM keyword " +
            " WHERE manga_id = ?1 ", nativeQuery = true)
    void deleteAllKeywordOfManga(Long id);
}
