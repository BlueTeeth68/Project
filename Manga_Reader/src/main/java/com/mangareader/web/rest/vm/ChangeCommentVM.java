package com.mangareader.web.rest.vm;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ChangeCommentVM {

    @NotNull
    private Long commentId;

    @NotNull
    @NotBlank
    private String content;

}
