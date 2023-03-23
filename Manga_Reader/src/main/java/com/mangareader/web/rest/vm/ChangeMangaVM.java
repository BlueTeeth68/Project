package com.mangareader.web.rest.vm;

import com.mangareader.domain.MangaStatus;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeMangaVM {

    @NotNull
    private Long id;

    private String name;

    private String summary;

    @Enumerated
    private MangaStatus status;

    @Min(1900)
    @Max(2100)
    private Integer yearOfPublication;

}
