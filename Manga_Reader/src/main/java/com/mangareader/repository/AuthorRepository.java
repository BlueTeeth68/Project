package com.mangareader.repository;

import com.mangareader.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findByUserId(Long userId);

    List<Author> findByNameContaining(String name);

}
