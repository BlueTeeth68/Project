package com.mangareader.service;

import com.mangareader.domain.Manga;

import java.util.List;

public interface IMangaService {

    Manga getMangaById(Long id);

    List<Manga> getMangaByGenre(Long genreId);

    List<Manga> getMangaByGenre(Long genreId, int limit, int offset);

    List<Manga> getMangaByAuthor(Long authorId, int limit, int offset);

    List<Manga> getMangaByTranslator(Long translatorId, int limit, int offset);

    List<Manga> getMangaByNameOrKeyword(String keyword, int limit, int offset);

    List<Manga> getSuggestMangas(int limit, int offset);

    List<Manga> getMangasByStatusLimit(String status, int limit, int offset);

    List<Manga> getMangaByName(String name);

    List<Manga> getAllManga();

    List<Manga> getAllMangaSortByLatestUpdate();

    List<Manga> getAllPaginateMangaOrderByLatestUpdate(int limit, int offset);

    Manga addGenreToManga(Long mangaId, String genreName);

    void deleteManga(Long id);

    Boolean existsById(Long id);


}
