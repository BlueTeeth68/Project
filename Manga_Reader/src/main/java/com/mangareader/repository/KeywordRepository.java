package com.mangareader.repository;

import com.mangareader.domain.Keyword;
import com.mangareader.domain.KeywordId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, KeywordId> {
}
