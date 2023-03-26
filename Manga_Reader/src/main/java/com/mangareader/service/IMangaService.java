package com.mangareader.service;

import com.mangareader.domain.Manga;
import com.mangareader.domain.MangaStatus;
import com.mangareader.web.rest.vm.ChangeMangaVM;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@SuppressWarnings("unused")
public interface IMangaService {

    Manga getMangaById(Long id);

    Manga getMangaById(String id);

    Page<Manga> getPageableMangaByGenre(Long genreId, int page, int size);

    Page<Manga> getPageableMangaByGenre(String genreId, String page, String size);

    Page<Manga> getPageableMangaByAuthor(Long authorId, int page, int size);

    Page<Manga> getPageableMangaByAuthor(String authorId, String page, String size);

    Page<Manga> getPageableMangaByTranslator(Long translatorId, int page, int size);

    Page<Manga> getPageableMangaByTranslator(String translatorId, String page, String size);

    Page<Manga> getPageableMangaByNameOrKeyword(String keyword, int page, int size);

    Page<Manga> getPageableMangaByNameOrKeyword(String keyword, String page, String size);

    Page<Manga> getPageableSuggestManga(int page, int size);

    Page<Manga> getPageableSuggestManga(String page, String size);

    Page<Manga> getPageableMangaByStatus(MangaStatus status, int page, int size);

    Page<Manga> getPageableMangaByStatus(String status, String page, String size);

    Page<Manga> getAllPageableMangaOrderByLatestUpdate(int page, int size);

    Page<Manga> getAllPageableMangaOrderByLatestUpdate(String page, String size);

    Manga createManga(Manga manga);

    Manga addGenreToManga(Long mangaId, Set<String> genreName, String serverName);

    Manga addAuthorsToManga(Long mangaId, Set<Long> authorIds, String serverName);

    Manga updateCoverImage(Long id, MultipartFile file);

    Manga updateCoverImage(String id, MultipartFile file, String serverName);

    void deleteManga(Long id);

    Resource getCoverImage(String fileName);

    Boolean existsById(Long id);

    void checkMangaAuthorize(Long mangaId);

    Manga changeMangaInformation(ChangeMangaVM vm);

    Manga saveManga(Manga manga);

    void increaseMangaView(Long mangaId);
}
