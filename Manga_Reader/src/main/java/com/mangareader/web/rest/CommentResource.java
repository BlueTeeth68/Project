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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Query successfully"),
        @ApiResponse(responseCode = "201", description = "Created successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request for input parameters", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized, missing or invalid JWT", content = @Content),
        @ApiResponse(responseCode = "403", description = "Access denied, do not have permission to access this resource", content = @Content),
})
public class CommentResource {

    private final ICommentService commentService;
    private final CommentMapper commentMapper;

    @Operation(
            summary = "Get all manga comment",
            description = "Any user can get all manga comment.", tags = "Comment",
            security = @SecurityRequirement(name = "authorize", scopes = "read"))
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
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

    @Operation(
            summary = "Comment on manga",
            description = "Logged in user can comment on a manga. " +
                    "The return result is all comment of manga.", tags = "Comment",
            security = @SecurityRequirement(name = "authorize"))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
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

    @Operation(
            summary = "Reply a comment on a post",
            description = "Logged in user can reply a comment on a post. " +
                    "The return result is all reply comment of original comment.", tags = "Comment",
            security = @SecurityRequirement(name = "authorize"))
    @PostMapping(value = "/reply", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
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

    @Operation(
            summary = "Change comment content",
            description = "Logged in user can change their comment content. " +
                    "The status of comment will be set to changed.", tags = "Comment",
            security = @SecurityRequirement(name = "authorize"))
    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentDTO> changeCommentContent(
            @Valid @RequestBody ChangeCommentVM vm
    ) {
        Comment comment = commentService.changeCommentContent(vm);
        CommentDTO commentDTO = commentMapper.toCommentDTO(comment);
        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Change reply comment content",
            description = "Logged in user can change their reply comment content. " +
                    "The reply comment status will be set to changed.", tags = "Comment",
            security = @SecurityRequirement(name = "authorize"))
    @PatchMapping(value = "/reply", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReplyCommentDTO> changeReplyCommentContent(
            @Valid @RequestBody ChangeCommentVM vm
    ) {
        ReplyComment replyComment = commentService.changeReplyCommentContent(vm);
        ReplyCommentDTO replyCommentDTO = commentMapper.toReplyCommentDTO(replyComment);
        return new ResponseEntity<>(replyCommentDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete comment",
            description = "Logged in user can delete their comment. " +
                    "The status this comment and reply comment of this comment will be set to deleted. " +
                    "Deleted comment won't be load from database.", tags = "Comment",
            security = @SecurityRequirement(name = "authorize"))
    @DeleteMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteComment(
            @RequestParam long id
    ) {
        commentService.deleteComment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Delete comment",
            description = "Logged in user can delete their reply comment. " +
                    "The status this reply comment will be set to deleted. " +
                    "Deleted comment won't be load from database.", tags = "Comment",
            security = @SecurityRequirement(name = "authorize"))
    @DeleteMapping("/reply")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteReplyComment(
            @RequestParam long id
    ) {
        commentService.deleteReplyComment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
