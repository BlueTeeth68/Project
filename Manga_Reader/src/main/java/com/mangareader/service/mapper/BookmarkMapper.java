package com.mangareader.service.mapper;

import com.mangareader.domain.Bookmark;
import com.mangareader.domain.Manga;
import com.mangareader.service.dto.BookmarkDTO;
import com.mangareader.service.dto.CommonMangaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class BookmarkMapper {

    private final MangaMapper mangaMapper;

    public BookmarkDTO toBookmarkDTO(Bookmark input, String serverName) {
        if (input == null)
            return null;
        BookmarkDTO result = new BookmarkDTO();
        result.setId(input.getId());
        Manga manga = input.getManga();
        CommonMangaDTO commonMangaDTO = mangaMapper.toCommonMangaDTO(manga, serverName);
        result.setCommonMangaDTO(commonMangaDTO);
        return result;
    }

    public List<BookmarkDTO> toListBookmarkDTO(List<Bookmark> input, String serverName) {
        if (input == null) {
            return null;
        }
        List<BookmarkDTO> bookmarkDTOS = new ArrayList<>();
        input.forEach(
                bookmark -> {
                    if (bookmark != null) {
                        bookmarkDTOS.add(toBookmarkDTO(bookmark, serverName));
                    }
                }
        );
        return bookmarkDTOS;
    }
}
