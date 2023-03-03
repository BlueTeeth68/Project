package com.mangareader.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KeywordDTO {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private Long mangaId;
}
