package com.mangareader.repository;

import com.mangareader.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findByActivate(Boolean activate);

    boolean existsByDisplayName(String displayName);

    @Query(value = "SELECT * FROM user " +
            " LIMIT ?1 OFFSET ?2 ", nativeQuery = true)
    List<User> findAllAndPaginateUser(int limit, int offset);
}
