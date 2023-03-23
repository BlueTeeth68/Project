package com.mangareader.web.rest.vm;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetGenreToMangaVM {

    @NotNull
    private Long mangaId;

    @NotNull
    private Set<String> genreName;
}
