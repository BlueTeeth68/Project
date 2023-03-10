package com.mangareader.repository;

import com.mangareader.domain.Keyword;
import com.mangareader.domain.KeywordId;
import com.mangareader.domain.Manga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, KeywordId> {

    List<Keyword> findByMangaIdOrderByName(Long mangaId);

    /*@Query(value = "SELECT m.* FROM manga m " +
            " WHERE m.id IN ( SELECT k.manga_id FROM keyword k" +
            " WHERE k.name = ?1 )", nativeQuery = true)  *//*LIKE CONCAT('%', ?1, '%')*//*
    List<Manga> findMangasByKeyword(String keyword);*/

    /*@Query(value = "SELECT m.* FROM manga m" +
            " INNER JOIN keyword k " +
            " ON m.id = k.manga_id " +
            " AND k.name LIKE CONCAT('%', ?1, '%')", nativeQuery = true)  *//*LIKE CONCAT('%', ?1, '%')*//*
    List<Manga> findMangasByKeyword(String keyword);*/

    // can not return another Entity type in KeywordRepository. This cause convert exception

/*    @Modifying
    @Query(value = " DELETE FROM keyword " +
            " WHERE manga_id = ?1 ", nativeQuery = true)
    void deleteAllKeywordOfManga(Long id);*/

    void deleteByMangaId(Long id);
}
