package com.mangareader.repository;

import com.mangareader.domain.Manga;
import com.mangareader.domain.MangaStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MangaRepository extends JpaRepository<Manga, Long> {

    List<Manga> findAllByOrderByLatestUpdateDesc();

    Page<Manga> findAllByOrderByLatestUpdateDesc(Pageable pageOption);

    @Query(value = " SELECT DISTINCT m FROM Manga m " +
            " WHERE m.name LIKE CONCAT('%', :keyword, '%') " +
            " OR m.id IN (SELECT DISTINCT k.manga.id FROM Keyword k " +
            " WHERE k.name LIKE CONCAT('%', :keyword, '%'))")
    Page<Manga> findPageableMangaByNameOrKeyword(@Param("keyword") String keyword, Pageable pageOption);

    @Query(value = " SELECT m FROM Manga m " +
            " JOIN m.genres g " +
            " WHERE g.id = :genreId ")
    Page<Manga> findPageableMangaByGenreId(@Param("genreId") Long id, Pageable pageOption);

    @Query(value = "SELECT m FROM Manga m " +
            " JOIN m.authors a " +
            " WHERE a.id = :authorId")
    Page<Manga> findPageableMangaByAuthorId(@Param("authorId") Long id, Pageable pageOption);

    Page<Manga> findByUserId(Long userId, Pageable pageOption);

    @Query(value = " SELECT m FROM Manga m " +
            " ORDER BY (m.view * m.rate) DESC ")
    Page<Manga> findPageableSuggestManga(Pageable pageOption);

    Page<Manga> findByStatus(MangaStatus status, Pageable pageOption);

}
