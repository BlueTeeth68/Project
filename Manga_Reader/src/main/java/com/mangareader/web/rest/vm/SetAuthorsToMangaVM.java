package com.mangareader.web.rest.vm;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetAuthorsToMangaVM {

    @NotNull
    private Long id;

    @NotNull
    private Set<Long> authorIds;

}
