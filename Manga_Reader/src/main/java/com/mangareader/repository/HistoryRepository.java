package com.mangareader.repository;

import com.mangareader.domain.History;
import com.mangareader.domain.HistoryId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, HistoryId> {

    Page<History> findByUserId(Long userId, Pageable pageOption);

}
