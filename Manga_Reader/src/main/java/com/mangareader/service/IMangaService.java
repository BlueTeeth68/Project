package com.mangareader.service;

import com.mangareader.domain.Manga;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface IMangaService {

    Manga getMangaById(Long id);

//    List<Manga> getMangaByGenre(Long genreId);

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

    Manga createManga(Manga manga);

    Manga addGenreToManga(Long mangaId, Set<String> genreName);

    Manga addAuthorsToManga(Long mangaId, Set<Long> authorIds);

    Manga updateCoverImage(Long id, MultipartFile file);

    void deleteManga(Long id);

    Resource getCoverImage(String fileName);

    Boolean existsById(Long id);
}
