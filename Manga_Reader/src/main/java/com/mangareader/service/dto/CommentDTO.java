package com.mangareader.service.dto;

import com.mangareader.domain.CommentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    private Long id;
    private String content;
    private CommentStatus status;
    private LocalDateTime createdDate;
    private CommonUserDTO user;
    private List<ReplyCommentDTO> commentDTOS = new ArrayList<>();
}
