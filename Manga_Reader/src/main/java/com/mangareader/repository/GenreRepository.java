package com.mangareader.repository;

import com.mangareader.domain.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findByName(String name);

    Boolean existsByName(String name);

    List<Genre> findAllByOrderByNameAsc();

    List<Genre> findByNameContainingOrderByNameAsc(String name);

    //old version
    @Query(value = "SELECT * FROM genre " +
            " ORDER BY name " +
            " LIMIT ?1 OFFSET ?2 ", nativeQuery = true)
    List<Genre> findLimitGenreAndSortByName(int limit, int offset);

    //new version
    @Query(value = "SELECT * FROM genre ", nativeQuery = true)
    Page<Genre> findAllGenreWithPageableSortByName(Pageable pageOption);

}
