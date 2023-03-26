package com.mangareader.service.mapper;

import com.mangareader.domain.Chapter;
import com.mangareader.domain.ChapterImage;
import com.mangareader.service.dto.ChapterDTO;
import com.mangareader.service.dto.ChapterImageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChapterMapper {

    public ChapterDTO toDTO(Chapter input) {
        if (input == null) {
            return null;
        }
        ChapterDTO result = new ChapterDTO();
        result.setId(input.getId());
        result.setName(input.getName());
        result.setChapterNumber(input.getChapterNumber());
        result.setCreatedDate(input.getCreatedDate());
        return result;
    }

    public List<ChapterDTO> toListDTO(List<Chapter> input) {
        if (input == null) {
            return null;
        }
        List<ChapterDTO> result = new ArrayList<>();
        input.forEach(
                chapter -> {
                    if (chapter != null) {
                        result.add(toDTO(chapter));
                    }
                }
        );
        return result;
    }

    public ChapterImageDTO toChapterImageDTO(Chapter input, String serverName) {

        if (input == null) {
            return null;
        }
        ChapterImageDTO result = new ChapterImageDTO();
        result.setId(input.getId());
        result.setName(input.getName());
        result.setChapterNumber(input.getChapterNumber());
        result.setCreatedDate(input.getCreatedDate());
        List<ChapterImage> chapterImages = input.getChapterImages();
        List<String> chapterImageUrls;
        if (chapterImages == null) {
            chapterImageUrls = null;
        } else {
            chapterImageUrls = new ArrayList<>();
            chapterImages.forEach(
                    chapterImage -> {
                        if (chapterImage != null) {
                            chapterImageUrls.add(serverName + chapterImage.getImageUrl());
                        }
                    }
            );
        }
        result.setChapterImageUrls(chapterImageUrls);
        return result;
    }
}
