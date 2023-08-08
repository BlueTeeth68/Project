package com.mangareader.service;

import com.mangareader.domain.Manga;
import com.mangareader.domain.MangaStatus;
import com.mangareader.web.rest.vm.ChangeMangaVM;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("unused")
public interface IMangaService {

    Manga getMangaById(Long id);

    Manga getMangaById(String id);

    Page<Manga> getPageableMangaByGenre(Long genreId, int page, int size);

    Page<Manga> getPageableMangaByAuthor(Long authorId, int page, int size);

    Page<Manga> getPageableMangaByTranslator(Long translatorId, int page, int size);

    Page<Manga> getPageableMangaByNameOrKeyword(String keyword, int page, int size);

    Page<Manga> getPageableSuggestManga(int page, int size);

    Page<Manga> getPageableMangaByStatus(MangaStatus status, int page, int size);

    Page<Manga> getAllPageableMangaOrderByLatestUpdate(int page, int size);

    Manga createManga(Manga manga);

    Manga addGenreToManga(Long mangaId, Set<String> genreName);

    Manga addAuthorsToManga(Long mangaId, Set<Long> authorIds);

    Manga updateCoverImage(Long id, MultipartFile file) throws TimeoutException;

    void deleteManga(Long id);

    boolean existsById(Long id);

    void checkMangaAuthorize(Long mangaId);

    Manga changeMangaInformation(ChangeMangaVM vm);

    Manga saveManga(Manga manga);

    void increaseMangaView(Long mangaId);

    Resource getCoverImage(String fileName);
}
