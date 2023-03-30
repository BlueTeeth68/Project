package com.mangareader.service.impl;

import com.mangareader.domain.*;
import com.mangareader.exception.BadRequestException;
import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.repository.CommentRepository;
import com.mangareader.repository.ReplyCommentRepository;
import com.mangareader.service.ICommentService;
import com.mangareader.service.IMangaService;
import com.mangareader.service.IUserService;
import com.mangareader.web.rest.vm.ChangeCommentVM;
import com.mangareader.web.rest.vm.CreateCommentVM;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements ICommentService {

    private final CommentRepository commentRepository;
    private final ReplyCommentRepository replyCommentRepository;
    private final IMangaService mangaService;
    private final IUserService userService;

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("There are no Comment " + id + " in the database.")
        );
    }

    @Override
    public ReplyComment getReplyCommentById(Long id) {
        return replyCommentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("There are no ReplyComment " + id + " in the database")
        );
    }

    @Override
    public Page<Comment> getCommentsOfManga(Long mangaId, int page, int size) {
        if (!mangaService.existsById(mangaId)) {
            throw new ResourceNotFoundException("Manga " + mangaId + " does not exist in database.");
        }

        if (page < 0) {
            throw new BadRequestException("Page must be greater than or equal to 0.");
        }
        if (size <= 0) {
            throw new BadRequestException("Size must be greater than 0.");
        }

        Pageable pageOption = PageRequest.of(page, size, Sort.by("createdDate").descending());
        List<CommentStatus> status = new ArrayList<>();
        status.add(CommentStatus.Active);
        status.add(CommentStatus.Changed);
        return commentRepository.findByMangaIdAndStatusIn(mangaId, status, pageOption);
    }

    @Override
    public Page<ReplyComment> getReplyCommentOfComment(Long commentId, int page, int size) {
        if (!existsCommentById(commentId)) {
            throw new ResourceNotFoundException("There are no comment " + commentId + " in the database.");
        }

        if (page < 0) {
            throw new BadRequestException("Page must be greater than or equal to 0.");
        }
        if (size <= 0) {
            throw new BadRequestException("Size must be greater than 0.");
        }

        Pageable pageOption = PageRequest.of(page, size, Sort.by("createdDate").descending());
        List<CommentStatus> status = new ArrayList<>();
        status.add(CommentStatus.Active);
        status.add(CommentStatus.Changed);
        return replyCommentRepository.findByCommentIdAndStatusIn(commentId, status, pageOption);
    }

    @Override
    public boolean existsCommentById(Long commentId) {
        return commentRepository.existsById(commentId);
    }

    @Override
    public boolean existsReplyCommentById(Long replyCommentId) {
        return replyCommentRepository.existsById(replyCommentId);
    }

    @Override
    public void createNewComment(CreateCommentVM vm) {

        Manga manga = mangaService.getMangaById(vm.getId());
        User user = userService.getCurrentUser();
        Comment comment = new Comment();
        comment.setContent(vm.getContent());
        comment.setStatus(CommentStatus.Active);
        comment.setCreatedDate(LocalDateTime.now());
        comment.setUser(user);
        comment.setManga(manga);
        commentRepository.save(comment);
    }

    @Override
    public void createReplyComment(CreateCommentVM vm) {
        if (!existsCommentById(vm.getId())) {
            throw new ResourceNotFoundException("There is no comment " + vm.getId() + " in the database.");
        }
        Comment comment = getCommentById(vm.getId());
        User user = userService.getCurrentUser();
        ReplyComment replyComment = new ReplyComment();
        replyComment.setContent(vm.getContent());
        replyComment.setStatus(CommentStatus.Active);
        replyComment.setCreatedDate(LocalDateTime.now());
        replyComment.setUser(user);
        replyComment.setComment(comment);
        replyCommentRepository.save(replyComment);
    }

    @Override
    public Comment changeCommentContent(ChangeCommentVM vm) {
        User user = userService.getCurrentUser();
        Comment comment = getCommentById(vm.getCommentId());
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Only owner can change comment.");
        }
        comment.setContent(vm.getContent());
        comment.setStatus(CommentStatus.Changed);
        return commentRepository.save(comment);
    }

    @Override
    public ReplyComment changeReplyCommentContent(ChangeCommentVM vm) {
        User user = userService.getCurrentUser();
        ReplyComment replyComment = getReplyCommentById(vm.getCommentId());
        if (!replyComment.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Only owner can change comment.");
        }
        replyComment.setContent(vm.getContent());
        replyComment.setStatus(CommentStatus.Changed);
        return replyCommentRepository.save(replyComment);
    }

    @Override
    public void deleteReplyComment(Long id) {
        User user = userService.getCurrentUser();
        ReplyComment replyComment = getReplyCommentById(id);
        if (!replyComment.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Only owner can change comment.");
        }
        replyComment.setStatus(CommentStatus.Deleted);
        replyCommentRepository.save(replyComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        User user = userService.getCurrentUser();
        Comment comment = getCommentById(id);
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Only owner can change comment.");
        }
        comment.setStatus(CommentStatus.Deleted);
        List<ReplyComment> replyComments = replyCommentRepository.findByCommentId(id);
        replyComments.forEach(
                replyComment -> replyComment.setStatus(CommentStatus.Deleted)
        );
        commentRepository.save(comment);
        replyCommentRepository.saveAll(replyComments);
    }
}
