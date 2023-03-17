package com.mangareader.service;

import com.mangareader.domain.Manga;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface IMangaService {

    Manga getMangaById(Long id);

    Manga getMangaById(String id);

//    List<Manga> getMangaByGenre(Long genreId);

    List<Manga> getMangaByGenre(Long genreId, int limit, int offset);

    List<Manga> getMangaByGenre(String genreId, String limit, String page);

    List<Manga> getMangaByAuthor(Long authorId, int limit, int offset);

    List<Manga> getMangaByAuthor(String authorId, String limit, String page);

    List<Manga> getMangaByTranslator(Long translatorId, int limit, int offset);

    List<Manga> getMangaByTranslator(String translatorId, String limit, String page);

    List<Manga> getMangaByNameOrKeyword(String keyword, int limit, int offset);

    List<Manga> getMangaByNameOrKeyword(String keyword, String limit, String page);

    List<Manga> getSuggestMangas(int limit, int offset);

    List<Manga> getSuggestMangas(String limit, String page);

    List<Manga> getMangasByStatusLimit(String status, int limit, int offset);

    List<Manga> getMangasByStatusLimit(String status, String limit, String page);

    List<Manga> getMangaByName(String name);

    List<Manga> getAllManga();

    List<Manga> getAllMangaSortByLatestUpdate();

    List<Manga> getAllPaginateMangaOrderByLatestUpdate(int limit, int offset);

    List<Manga> getAllPaginateMangaOrderByLatestUpdate(String limit, String page);

    Manga createManga(Manga manga);

    Manga addGenreToManga(Long mangaId, Set<String> genreName, String serverName);

    Manga addAuthorsToManga(Long mangaId, Set<Long> authorIds, String serverName);

    Manga updateCoverImage(Long id, MultipartFile file, String serverName);

    Manga updateCoverImage(String id, MultipartFile file, String serverName);

    void deleteManga(Long id);

    Resource getCoverImage(String fileName);

    Boolean existsById(Long id);

    void checkMangaAuthorize(Long mangaId);
}
