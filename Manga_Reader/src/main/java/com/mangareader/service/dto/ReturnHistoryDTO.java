package com.mangareader.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnHistoryDTO {

    private CommonMangaDTO commonMangaDTO;
    private ChapterDTO chapterDTO;
    private LocalDateTime date;

}
