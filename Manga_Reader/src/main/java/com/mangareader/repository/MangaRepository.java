package com.mangareader.repository;

import com.mangareader.domain.Manga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MangaRepository extends JpaRepository<Manga, Long> {

    List<Manga> findAllByOrderByLatestUpdateDesc();

    @Query(value = "SELECT * FROM manga " +
            " ORDER BY latest_update DESC" +
            " LIMIT ?1 OFFSET ?2 ", nativeQuery = true)
    List<Manga> findAllAndPaginateOrderByLatestUpdate(int limit, int offset);

    List<Manga> findByNameContainingOrderByName(String name);

    @Query(value = " SELECT DISTINCT * FROM manga m " +
            " WHERE m.name LIKE '%' + ?1 + '%' " +
            " OR m.id IN (SELECT DISTINCT manga_id FROM keyword k " +
            " WHERE k.name LIKE '%' + ?1 + '%' " +
            " LIMIT ?2 OFFSET ?3) ", nativeQuery = true)
    List<Manga> findByNameOrKeywordOrderByName(String keyword, int limit, int offset);

/*    @Query(value = " SELECT * FROM manga m " +
            " WHERE m.name LIKE '%' + ?1 + '%' " +
            " UNION " +
            " SELECT * FROM manga m " +
            " WHERE m.id IN ( " +
            " SELECT DISTINCT k.manga_id FROM keyword k " +
            " WHERE k.name LIKE '%' + ?1 + '%' )", nativeQuery = true)
    List<Manga> findByNameOrKeywordOrderByName(String keyword);*/

    @Query(value = "SELECT m.* FROM manga m " +
            " INNER JOIN manga_genre mg ON " +
            " m.id = mg.manga_id AND mg.genre_id = ?1 " +
            " LIMIT ?2 OFFSET ?3 ", nativeQuery = true)
    List<Manga> findLimitMangaByGenreID(Long id, int limit, int offset);

    @Query(value = "SELECT m.* FROM manga m " +
            " INNER JOIN manga_author ma ON " +
            " m.id = ma.manga_id AND ma.author_id = ?1 " +
            " LIMIT ?2 OFFSET ?3 ", nativeQuery = true)
    List<Manga> findLimitMangaByAuthorId(Long id, int limit, int offset);

    @Query(value = "SELECT * FROM manga " +
            " WHERE user_id = ?1 " +
            " LIMIT ?2 OFFSET ?3 ", nativeQuery = true)
    List<Manga> findByUserIdOrderByName(Long id, int limit, int offset);

    @Query(value = " SELECT * FROM manga m" +
            " ORDER BY (m.view * m.rate) DESC " +
            " LIMIT ?1 OFFSET ?2 ", nativeQuery = true)
    List<Manga> findSuggestManga(int limit, int offset);

    @Query(value = "SELECT * FROM manga m " +
            " WHERE m.status = ?1 " +
            " LIMIT ?2 OFFSET ?3 ", nativeQuery = true)
    List<Manga> findMangaByStatusLimit(String status, int limit, int offset);

}
