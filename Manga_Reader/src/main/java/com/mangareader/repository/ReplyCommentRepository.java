package com.mangareader.repository;

import com.mangareader.domain.CommentStatus;
import com.mangareader.domain.ReplyComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SuppressWarnings("unused")
public interface ReplyCommentRepository extends JpaRepository<ReplyComment, Long> {

    Page<ReplyComment> findByCommentIdAndStatus(Long commentId, CommentStatus status, Pageable pageOption);

    Page<ReplyComment> findByCommentIdAndStatusIn(Long commentId, List<CommentStatus> status, Pageable pageOption);

    Page<ReplyComment> findByStatus(CommentStatus status, Pageable pageOption);

    Page<ReplyComment> findByStatusIn(List<CommentStatus> status, Pageable pageOption);
}
