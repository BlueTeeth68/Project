package com.mangareader.service.mapper;

import com.mangareader.domain.Manga;
import com.mangareader.service.IMangaService;
import com.mangareader.service.dto.AuthorDTO;
import com.mangareader.service.dto.MangaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MangaMapper {
    private final IMangaService mangaService;

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
        input.getGenres().forEach(genre -> {
            genres.add(genre.getName());
        });
        result.setGenres(genres);
        Set<AuthorDTO> authors = new HashSet<>();
        input.getAuthors().forEach(author -> {
            AuthorDTO tmp = new AuthorDTO();
            tmp.setId(author.getId());
            tmp.setName(author.getName());
            authors.add(tmp);
        });
        result.setAuthors(authors);
        return result;
    }

    public Manga toEntity(MangaDTO input) {
        if (input.getId() == null) {
            return null;
        }
        Manga result = mangaService.getMangaById(input.getId());
        return result;
    }

}
