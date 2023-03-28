package com.mangareader.service.dto;

import com.mangareader.domain.CommentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCommentDTO {

    private Long id;
    private String content;
    private CommentStatus status;
    private LocalDateTime createdDate;
    private CommonUserDTO user;
    private Long commentId;
}
