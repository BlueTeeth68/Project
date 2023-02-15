package com.mangareader.repository;

import com.mangareader.domain.History;
import com.mangareader.domain.HistoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, HistoryId> {
}
