package com.mangareader.service;

import com.mangareader.domain.Manga;

import java.util.List;

public interface IMangaService {

    Manga getMangaById(Long id);

    List<Manga> getMangaByGenre(Long genreId);

    List<Manga> getMangaByNameOrKeyword(String keyword);

    List<Manga> getAllMange();

    List<Manga> getAllMangaSortByLatestUpdate();

    List<Manga> getAllPaginateMangaOrderByLatestUpdate(int limit, int offset);

    Manga addGenreToManga(Long mangaId, String genreName);

    void deleteManga(Long id);


}
