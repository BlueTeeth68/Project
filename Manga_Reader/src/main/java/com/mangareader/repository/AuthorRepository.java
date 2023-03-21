package com.mangareader.repository;

import com.mangareader.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findByUser(Long userId);

    List<Author> findByNameContaining(String name);

    //old version
    @Query(value = "SELECT * FROM author " +
            " ORDER BY name " +
            " LIMIT ?1 OFFSET ?2 ", nativeQuery = true)
    List<Author> findLimitAuthor(int limit, int offset);

//    //new version
//    Page<Author> findAll(Pageable pageOption);
}
