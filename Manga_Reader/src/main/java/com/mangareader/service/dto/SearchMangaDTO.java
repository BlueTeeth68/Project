package com.mangareader.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchMangaDTO {
    private Long id;

    private String name;

    private String coverImageUrl;

    private Float rate;
}
