package com.mangareader.web.rest.vm;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeywordMangaVM {

    @NotNull
    private Long mangaId;

    @NotNull
    private List<String> keywords;
}
