package com.mangareader.service.impl;

import com.mangareader.domain.Genre;
import com.mangareader.domain.Manga;
import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.repository.MangaRepository;
import com.mangareader.service.IGenreService;
import com.mangareader.service.IMangaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MangaServiceImpl implements IMangaService {

    @Autowired
    private MangaRepository mangaRepository;

    @Autowired
    private IGenreService genreService;

    @Override
    public Manga getMangaById(Long id) {
        return mangaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Manga " + id + " does not exist.")
        );
    }

    @Override
    public List<Manga> getMangaByGenre(Long genreId) {
        Genre genre = genreService.getGenreById(genreId);
        List<Manga> result = genre.getMangas().stream().toList();
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("There are no mange with genre " + genre.getName());
        }
        return result;
    }

    @Override
    public List<Manga> getMangaByNameOrKeyword(String keyword) {
        return null;
    }

    @Override
    public List<Manga> getAllMange() {
        log.info("Get all manga from database sorted by id.");
        List<Manga> result = mangaRepository.findAll();
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("There are no manga in the database.");
        }
        return result;
    }

    @Override
    public List<Manga> getAllMangaSortByLatestUpdate() {
        log.info("Get all manga from database sorted by latest update desc.");
        List<Manga> result = mangaRepository.findAllByOrderByLatestUpdateDesc();

        if (result.isEmpty()) {
            throw new ResourceNotFoundException("There are no manga in the database.");
        }

        return result;
    }

    @Override
    public List<Manga> getAllPaginateMangaOrderByLatestUpdate(int limit, int offset) {

        List<Manga> result = mangaRepository.findAllAndPaginateOrderByLatestUpdate(limit, offset);

        return result;
    }

    @Override
    public Manga addGenreToManga(Long mangaId, String genreName) {
        Manga manga = getMangaById(mangaId);
        Genre genre = genreService.getGenreByName(genreName);
        manga.getGenres().add(genre);
        mangaRepository.save(manga);

        return manga;
    }

    @Override
    public void deleteManga(Long id) {

    }
}
