package com.mangareader.repository;

import com.mangareader.domain.Manga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MangaRepository extends JpaRepository<Manga, Long> {

    List<Manga> findAllByOrderByLatestUpdateDesc();

    @Query (value = "SELECT * FROM manga " +
            " ORDER BY latest_update DESC" +
            " LIMIT ?1 OFFSET ?2 ", nativeQuery = true)
    List<Manga> findAllAndPaginateOrderByLatestUpdate(int limit, int offset);
}
