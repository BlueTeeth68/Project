package com.mangareader.repository;

import com.mangareader.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByDisplayName(String displayName);

    boolean existsByUsername(String username);

    List<User> findByActivate(Boolean activate);

    boolean existsByDisplayName(String displayName);

    Page<User> findAll(Pageable pageOption);
}
