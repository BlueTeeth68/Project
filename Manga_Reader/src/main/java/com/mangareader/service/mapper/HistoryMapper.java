package com.mangareader.service.mapper;

import com.mangareader.domain.Chapter;
import com.mangareader.domain.History;
import com.mangareader.domain.Manga;
import com.mangareader.service.dto.ChapterDTO;
import com.mangareader.service.dto.CommonMangaDTO;
import com.mangareader.service.dto.ReturnHistoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryMapper {

    private final MangaMapper mangaMapper;
    private final ChapterMapper chapterMapper;

    public ReturnHistoryDTO toReturnHistoryDTO(History history) {
        if (history == null)
            return null;
        Manga manga = history.getManga();
        Chapter chapter = history.getChapter();
        CommonMangaDTO commonMangaDTO = mangaMapper.toCommonMangaDTO(manga);
        ChapterDTO chapterDTO = chapterMapper.toDTO(chapter);
        ReturnHistoryDTO result = new ReturnHistoryDTO();
        result.setCommonMangaDTO(commonMangaDTO);
        result.setChapterDTO(chapterDTO);
        result.setDate(history.getDate());
        return result;
    }

    public List<ReturnHistoryDTO> toListReturnHistoryDTO(List<History> histories) {
        List<ReturnHistoryDTO> result = new ArrayList<>();
        if (histories != null) {
            histories.forEach(
                    history -> {
                        if (history != null) {
                            result.add(toReturnHistoryDTO(history));
                        }
                    }
            );
        }
        return result;
    }
}
