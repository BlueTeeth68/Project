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

    //new version
    @Query(value = "SELECT g FROM Genre g")
    Page<Genre> findAllGenreWithPageableSortByName(Pageable pageOption);

}
