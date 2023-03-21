package com.mangareader.service.mapper;

import com.mangareader.domain.Keyword;
import com.mangareader.domain.Manga;
import com.mangareader.service.IMangaService;
import com.mangareader.service.dto.KeywordDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused")
public class KeywordMapper {
    private final IMangaService mangaService;

    public KeywordDTO toDTO(Keyword input) {
        KeywordDTO result = null;

        if (input != null) {
            log.info("Converting keyword to keywordDTO");
            result = new KeywordDTO();
            result.setName(input.getName());
            result.setMangaId(input.getManga().getId());
        }

        return result;
    }

    public Keyword toEntity(KeywordDTO input) {
        Keyword result = null;

        if (input != null) {
            Manga manga = mangaService.getMangaById(input.getMangaId());
            result = new Keyword();
            result.setName(input.getName());
            result.setManga(manga);
        }

        return result;
    }
}
