package com.mangareader.service;

import com.mangareader.domain.Manga;

import java.util.List;

public interface IMangaService {

    public Manga getMangaById(Long id);

    public List<Manga> getMangaByGenre(Long genreId);

    public List<Manga> getMangaByNameOrKeyword(String keyword);

    public List<Manga> getAllMange();

    public List<Manga> getAllMangaSortByLatestUpdate();

    public Manga addGenreToManga(Long mangaId, String genreName);

    public void deleteManga(Long id);


}
