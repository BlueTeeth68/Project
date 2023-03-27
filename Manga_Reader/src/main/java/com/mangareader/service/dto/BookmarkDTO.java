package com.mangareader.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkDTO {
    private Long id;

    private CommonMangaDTO commonMangaDTO;
}
