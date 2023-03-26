package com.mangareader.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterImageDTO {

    Long id;

    String name;

    Float chapterNumber;

    LocalDateTime createdDate;

    List<String> chapterImageUrls;
}
