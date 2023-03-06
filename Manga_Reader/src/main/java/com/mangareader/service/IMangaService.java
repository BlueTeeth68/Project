package com.mangareader.service;

import com.mangareader.domain.Manga;

import java.util.List;

public interface IMangaService {

    Manga getMangaById(Long id);

    List<Manga> getMangaByGenre(Long genreId);

    List<Manga> getMangaByGenre(Long genreID, int limit, int offset);

    List<Manga> getMangaByNameOrKeyword(String keyword, int limit, int offset);

    List<Manga> getMangaByName(String name);

    List<Manga> getAllMange();

    List<Manga> getAllMangaSortByLatestUpdate();

    List<Manga> getAllPaginateMangaOrderByLatestUpdate(int limit, int offset);

    Manga addGenreToManga(Long mangaId, String genreName);

    void deleteManga(Long id);

    Boolean existsById(Long id);


}
