package com.mangareader.web.rest.vm;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMangaVM {

    @NotNull
    @NotBlank
    @Size(min = 1, max = 256)
    private String name;

    @Size(max = 1000)
    private String summary;

    @NotNull
    @Min(1900)
    @Max(2100)
    private Integer yearOfPublication;
}
