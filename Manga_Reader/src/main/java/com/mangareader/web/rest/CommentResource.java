package com.mangareader.web.rest;

import com.mangareader.domain.Comment;
import com.mangareader.domain.ReplyComment;
import com.mangareader.service.ICommentService;
import com.mangareader.service.dto.CommentDTO;
import com.mangareader.service.dto.PagingReturnDTO;
import com.mangareader.service.dto.ReplyCommentDTO;
import com.mangareader.service.mapper.CommentMapper;
import com.mangareader.web.rest.vm.ChangeCommentVM;
import com.mangareader.web.rest.vm.CreateCommentVM;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manga/comment")
@RequiredArgsConstructor
@EnableMethodSecurity
@SuppressWarnings("unused")
public class CommentResource {

    private final ICommentService commentService;
    private final CommentMapper commentMapper;

    @GetMapping()
    public ResponseEntity<PagingReturnDTO<CommentDTO>> getALlCommentOfManga(
            @RequestParam(required = false, defaultValue = "0") Long mangaId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Page<Comment> comments = commentService.getCommentsOfManga(mangaId, page, size);
        List<CommentDTO> commentDTOs = commentMapper.toListCommentDTO(comments.getContent());

        PagingReturnDTO<CommentDTO> result = new PagingReturnDTO<>();
        result.setContent(commentDTOs);
        result.setTotalPages(comments.getTotalPages());
        result.setTotalElements(comments.getTotalElements());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "authorize")
    public ResponseEntity<PagingReturnDTO<CommentDTO>> createComment(
            @Valid @RequestBody CreateCommentVM vm,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        //create new comment
        commentService.createNewComment(vm);
        //reload comment
        Page<Comment> comments = commentService.getCommentsOfManga(vm.getId(), page, size);
        List<CommentDTO> commentDTOs = commentMapper.toListCommentDTO(comments.getContent());

        PagingReturnDTO<CommentDTO> result = new PagingReturnDTO<>();
        result.setContent(commentDTOs);
        result.setTotalPages(comments.getTotalPages());
        result.setTotalElements(comments.getTotalElements());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/reply")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "authorize")
    public ResponseEntity<PagingReturnDTO<ReplyCommentDTO>> replyComment(
            @Valid @RequestBody CreateCommentVM vm,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        //create new comment
        commentService.createReplyComment(vm);
        //reload reply comment
        Page<ReplyComment> replyComments = commentService.getReplyCommentOfComment(vm.getId(), page, size);
        List<ReplyCommentDTO> replyCommentDTOs = commentMapper.toListReplyCommonDTO(replyComments.getContent());

        PagingReturnDTO<ReplyCommentDTO> result = new PagingReturnDTO<>();
        result.setContent(replyCommentDTOs);
        result.setTotalPages(replyComments.getTotalPages());
        result.setTotalElements(replyComments.getTotalElements());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping()
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "authorize")
    public ResponseEntity<CommentDTO> changeCommentContent(
            @Valid @RequestBody ChangeCommentVM vm
    ) {
        Comment comment = commentService.changeCommentContent(vm);
        CommentDTO commentDTO = commentMapper.toCommentDTO(comment);
        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
    }

    @PatchMapping("/reply")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "authorize")
    public ResponseEntity<ReplyCommentDTO> changeReplyCommentContent(
            @Valid @RequestBody ChangeCommentVM vm
    ) {
        ReplyComment replyComment = commentService.changeReplyCommentContent(vm);
        ReplyCommentDTO replyCommentDTO = commentMapper.toReplyCommentDTO(replyComment);
        return new ResponseEntity<>(replyCommentDTO, HttpStatus.OK);
    }

    @DeleteMapping()
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "authorize")
    public ResponseEntity<?> deleteComment(
            @RequestParam long id
    ) {
        commentService.deleteComment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/reply")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "authorize")
    public ResponseEntity<?> deleteReplyComment(
            @RequestParam long id
    ) {
        commentService.deleteReplyComment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
