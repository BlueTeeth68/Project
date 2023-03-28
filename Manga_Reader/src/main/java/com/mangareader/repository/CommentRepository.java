package com.mangareader.repository;

import com.mangareader.domain.Comment;
import com.mangareader.domain.CommentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SuppressWarnings("unused")
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByMangaIdAndStatus(Long mangaId, CommentStatus status, Pageable pageOption);

    Page<Comment> findByMangaIdAndStatusIn(Long mangaId, List<CommentStatus> status, Pageable pageOption);

    Page<Comment> findByStatus(CommentStatus status, Pageable pageOption);

    Page<Comment> findByStatusIn(List<CommentStatus> status, Pageable pageOption);

}
