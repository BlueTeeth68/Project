package com.mangareader.service.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PagingReturnDTO<T> {

    private List<T> content;

    @NotNull
    private Long totalElements;

    @NotNull
    private Integer totalPages;
}
