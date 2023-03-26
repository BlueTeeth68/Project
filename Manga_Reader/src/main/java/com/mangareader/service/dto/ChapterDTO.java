package com.mangareader.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterDTO {

    private Long id;

    private String name;

    private Float chapterNumber;

    private LocalDateTime createdDate;
}
