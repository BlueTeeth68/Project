package com.mangareader.service;

import com.mangareader.domain.Comment;
import com.mangareader.domain.ReplyComment;
import com.mangareader.web.rest.vm.ChangeCommentVM;
import com.mangareader.web.rest.vm.CreateCommentVM;
import org.springframework.data.domain.Page;

@SuppressWarnings("unused")
public interface ICommentService {

    Comment getCommentById(Long id);

    ReplyComment getReplyCommentById(Long id);

    Page<Comment> getCommentsOfManga(Long mangaId, int page, int size);

    Page<ReplyComment> getReplyCommentOfComment(Long commentId, int page, int size);

    boolean existsCommentById(Long commentId);

    boolean existsReplyCommentById(Long replyCommentId);

    void createNewComment(CreateCommentVM vm);

    void createReplyComment(CreateCommentVM vm);

    Comment changeCommentContent(ChangeCommentVM vm);

    ReplyComment changeReplyCommentContent(ChangeCommentVM vm);

    void deleteReplyComment(Long id);

    void deleteComment(Long id);
}
