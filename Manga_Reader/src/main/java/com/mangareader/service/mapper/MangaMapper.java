package com.mangareader.service.mapper;

import com.mangareader.domain.Chapter;
import com.mangareader.domain.Manga;
import com.mangareader.service.IChapterService;
import com.mangareader.service.IMangaService;
import com.mangareader.service.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class MangaMapper {
    private final IMangaService mangaService;

    private final IChapterService chapterService;

    private final ChapterMapper chapterMapper;

    public MangaDTO toDTO(Manga input, String serverName) {
        MangaDTO result = new MangaDTO();
        result.setId(input.getId());
        result.setName(input.getName());
        if (input.getCoverImageUrl() != null) {
            result.setCoverImageUrl(serverName + input.getCoverImageUrl());
        }
        result.setView(input.getView());
        result.setSummary(input.getSummary());
        result.setRate(input.getRate());
        result.setTotalVote(input.getTotalVote());
        result.setLatestUpdate(input.getLatestUpdate());
        result.setStatus(input.getStatus());
        result.setYearOfPublication(input.getYearOfPublication());
        if (input.getUser() != null) {
            result.setUser_id(input.getUser().getId());
        }
        Set<String> genres = new HashSet<>();
        input.getGenres().forEach(
                genre -> genres.add(genre.getName())
        );
        result.setGenres(genres);
        Set<AuthorDTO> authors = new HashSet<>();
        input.getAuthors().forEach(author -> {
            AuthorDTO tmp = new AuthorDTO();
            tmp.setId(author.getId());
            tmp.setName(author.getName());
            authors.add(tmp);
        });
        result.setAuthors(authors);

        List<Chapter> chapters = chapterService.getChapterByMangaId(input.getId());
        List<ChapterDTO> chapterDTOS = chapterMapper.toListDTO(chapters);
        result.setChapters(chapterDTOS);
        return result;
    }

    public List<MangaDTO> toListDTO(List<Manga> input, String serverName) {
        List<MangaDTO> result = new ArrayList<>();
        if (input == null) {
            return null;
        }
        input.forEach(
                manga -> result.add(toDTO(manga, serverName))
        );
        return result;
    }

    public Manga toEntity(MangaDTO input) {
        if (input.getId() == null) {
            return null;
        }
        return mangaService.getMangaById(input.getId());
    }

    public CommonMangaDTO toCommonMangaDTO(Manga input, String serverName) {
        CommonMangaDTO result = new CommonMangaDTO();
        result.setId(input.getId());
        result.setName(input.getName());
        if (input.getCoverImageUrl() != null) {
            result.setCoverImageUrl(serverName + input.getCoverImageUrl());
        }
        result.setRate(input.getRate());
        result.setView(input.getView());
        return result;
    }

    public List<CommonMangaDTO> toListCommonMangaDTO(List<Manga> input, String serverName) {
        List<CommonMangaDTO> result = new ArrayList<>();
        if (input == null) {
            return null;
        }
        input.forEach(
                manga -> result.add(toCommonMangaDTO(manga, serverName))
        );
        return result;
    }

    public PagingReturnDTO<MangaDTO> toPagingReturnDTOMangaDTO(Page<Manga> mangas, String serverName) {
        List<MangaDTO> mangaDTOs = toListDTO(mangas.getContent(), serverName);
        PagingReturnDTO<MangaDTO> result = new PagingReturnDTO<>();
        result.setTotalPages(mangas.getTotalPages());
        result.setTotalElements(mangas.getTotalElements());
        result.setContent(mangaDTOs);
        return result;
    }

    public PagingReturnDTO<CommonMangaDTO> toPagingReturnDTOSearchMangaDTO(Page<Manga> mangas, String serverName) {
        List<CommonMangaDTO> commonMangaDTOS = toListCommonMangaDTO(mangas.getContent(), serverName);
        PagingReturnDTO<CommonMangaDTO> result = new PagingReturnDTO<>();
        result.setTotalPages(mangas.getTotalPages());
        result.setTotalElements(mangas.getTotalElements());
        result.setContent(commonMangaDTOS);
        return result;
    }


}
