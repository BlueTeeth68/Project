package com.mangareader.service.mapper;

import com.mangareader.domain.Comment;
import com.mangareader.domain.CommentStatus;
import com.mangareader.domain.ReplyComment;
import com.mangareader.domain.User;
import com.mangareader.service.dto.CommentDTO;
import com.mangareader.service.dto.CommonUserDTO;
import com.mangareader.service.dto.ReplyCommentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentMapper {

    private final UserMapper userMapper;

    public ReplyCommentDTO toReplyCommentDTO(ReplyComment input) {
        if (input == null) {
            return null;
        }
        ReplyCommentDTO result = new ReplyCommentDTO();
        result.setId(input.getId());
        result.setContent(input.getContent());
        result.setStatus(input.getStatus());
        result.setCreatedDate(input.getCreatedDate());
        User user = input.getUser();
        CommonUserDTO commonUserDTO = userMapper.toCommonUserDTO(user);
        result.setUser(commonUserDTO);
        result.setCommentId(input.getComment().getId());
        return result;
    }

    public List<ReplyCommentDTO> toListReplyCommonDTO(List<ReplyComment> input) {
        if (input == null)
            return null;
        List<ReplyCommentDTO> result = new ArrayList<>();
        input.forEach(
                replyComment -> {
                    if (replyComment != null && replyComment.getStatus() != CommentStatus.Deleted) {
                        result.add(toReplyCommentDTO(replyComment));
                    }
                }
        );
        return result;
    }

    public CommentDTO toCommentDTO(Comment input) {
        if (input == null)
            return null;
        CommentDTO result = new CommentDTO();
        result.setId(input.getId());
        result.setContent(input.getContent());
        result.setStatus(input.getStatus());
        result.setCreatedDate(input.getCreatedDate());
        User user = input.getUser();
        CommonUserDTO commonUserDTO = userMapper.toCommonUserDTO(user);
        result.setUser(commonUserDTO);


        List<ReplyCommentDTO> replyCommentDTOs = toListReplyCommonDTO(input.getReplyComments());
        result.setCommentDTOS(replyCommentDTOs);
        return result;
    }

    public List<CommentDTO> toListCommentDTO(List<Comment> input) {
        if (input == null) {
            return null;
        }
        List<CommentDTO> result = new ArrayList<>();
        input.forEach(
                comment -> {
                    if (comment != null) {
                        result.add(toCommentDTO(comment));
                    }
                }
        );
        return result;
    }
}
