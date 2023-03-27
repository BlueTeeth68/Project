package com.mangareader.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonMangaDTO {
    private Long id;

    private String name;

    private String coverImageUrl;

    private Float rate;

    private Integer view;
}
