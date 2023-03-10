package com.mangareader.repository;

import com.mangareader.domain.ReplyComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyCommentRepository extends JpaRepository<ReplyComment, Long> {
}
