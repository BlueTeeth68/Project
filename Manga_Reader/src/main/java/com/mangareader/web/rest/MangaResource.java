package com.mangareader.web.rest;

import com.mangareader.domain.MangaStatus;
import com.mangareader.service.util.APIUtil;
import com.mangareader.domain.Manga;
import com.mangareader.service.IMangaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/manga")
@RequiredArgsConstructor
public class MangaResource {

    private final IMangaService mangaService;

    private final HttpServletRequest request;

    @GetMapping("/list")
    public ResponseEntity<List<Manga>> getAllPaginateManga(
            @RequestParam(required = false, defaultValue = "100") String limit,
            @RequestParam(required = false, defaultValue = "1") String page
    ) {
        List<Manga> mangas;

        int limitNum = APIUtil.parseStringToInteger(limit, "Limit is not a number exception.");
        int pageNum = APIUtil.parseStringToInteger(page, "Page is not a number exception");
        int offset = limitNum * (pageNum - 1);

        mangas = mangaService.getAllPaginateMangaOrderByLatestUpdate(limitNum, offset);

        mangas = addServerNameToCoverImage(mangas);

        return ResponseEntity.ok(mangas);
    }

    @GetMapping()
    public ResponseEntity<Manga> getMangaById(
            @RequestParam(defaultValue = "1") String id
    ) {
        Long idNum = APIUtil.parseStringToLong(id, "id is not a number.");
        Manga manga = mangaService.getMangaById(idNum);
        return new ResponseEntity<>(manga, HttpStatus.FOUND);
    }

    @GetMapping("/genre")
    public ResponseEntity<List<Manga>> getMangaByGenre(
            @RequestParam(defaultValue = "1") String id,
            @RequestParam(defaultValue = "20") String limit,
            @RequestParam(defaultValue = "1") String page
    ) {
        Long genreIdNum = APIUtil.parseStringToLong(id, "genreId is not a number.");
        int limitNum = APIUtil.parseStringToInteger(limit, "limit is not a number.");
        int pageNum = APIUtil.parseStringToInteger(page, "page is not a number.");
        int offset = limitNum * (pageNum - 1);
        List<Manga> mangas = mangaService.getMangaByGenre(genreIdNum, limitNum, offset);
        mangas = addServerNameToCoverImage(mangas);
        return new ResponseEntity<>(mangas, HttpStatus.FOUND);
    }

    @GetMapping("/author")
    public ResponseEntity<List<Manga>> getMangaByAuthor(
            @RequestParam(defaultValue = "1") String id,
            @RequestParam(defaultValue = "20") String limit,
            @RequestParam(defaultValue = "1") String page
    ) {
        Long authorIdNum = APIUtil.parseStringToLong(id, "authorId is not a number.");
        int limitNum = APIUtil.parseStringToInteger(limit, "limit is not a number.");
        int pageNum = APIUtil.parseStringToInteger(page, "page is not a number.");
        int offset = limitNum * (pageNum - 1);
        List<Manga> mangas = mangaService.getMangaByAuthor(authorIdNum, limitNum, offset);
        mangas = addServerNameToCoverImage(mangas);
        return new ResponseEntity<>(mangas, HttpStatus.FOUND);
    }

    @GetMapping("/name")
    public ResponseEntity<List<Manga>> getMangasByNameOrKeywordOrderByNameWithPagination(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "20") String limit,
            @RequestParam(defaultValue = "1") String page
    ) {
        int limitNum = APIUtil.parseStringToInteger(limit, "limit is not a number.");
        int pageNum = APIUtil.parseStringToInteger(page, "page is not a number.");
        int offset = limitNum * (pageNum - 1);
        List<Manga> mangas = mangaService.getMangaByNameOrKeyword(keyword, limitNum, offset);
        mangas = addServerNameToCoverImage(mangas);
        return new ResponseEntity<>(mangas, HttpStatus.FOUND);
    }

    @GetMapping("/translator")
    public ResponseEntity<List<Manga>> getMangaByTranslator(
            @RequestParam(defaultValue = "1") String id,
            @RequestParam(defaultValue = "20") String limit,
            @RequestParam(defaultValue = "1") String page
    ) {
        Long translatorId = APIUtil.parseStringToLong(id, "translatorId is not a number.");
        int limitNum = APIUtil.parseStringToInteger(limit, "limit is not a number.");
        int pageNum = APIUtil.parseStringToInteger(page, "page is not a number.");
        int offset = limitNum * (pageNum - 1);
        List<Manga> mangas = mangaService.getMangaByTranslator(translatorId, limitNum, offset);
        mangas = addServerNameToCoverImage(mangas);
        return new ResponseEntity<>(mangas, HttpStatus.FOUND);
    }

    @GetMapping("/suggest")
    public ResponseEntity<List<Manga>> getSuggestMangas(
            @RequestParam(defaultValue = "20") String limit,
            @RequestParam(defaultValue = "1") String page
    ) {
        int limitNum = APIUtil.parseStringToInteger(limit, "limit is not a number.");
        int pageNum = APIUtil.parseStringToInteger(page, "page is not a number.");
        int offset = limitNum * (pageNum - 1);
        List<Manga> mangas = mangaService.getSuggestMangas(limitNum, offset);
        mangas = addServerNameToCoverImage(mangas);
        return new ResponseEntity<>(mangas, HttpStatus.FOUND);
    }

    @GetMapping("/status")
    public ResponseEntity<List<Manga>> getMangasByStatus(
            @RequestParam String status,
            @RequestParam(defaultValue = "20") String limit,
            @RequestParam(defaultValue = "1") String page
    ) {
        MangaStatus mangaStatus = APIUtil.parseStringToMangaStatus(status, "Status must be Ongoing or Completed");
        int limitNum = APIUtil.parseStringToInteger(limit, "limit is not a number.");
        int pageNum = APIUtil.parseStringToInteger(page, "page is not a number.");
        int offset = limitNum * (pageNum - 1);
        List<Manga> mangas = mangaService.getMangasByStatusLimit(mangaStatus.toString(), limitNum, offset);
        mangas = addServerNameToCoverImage(mangas);
        return new ResponseEntity<>(mangas, HttpStatus.FOUND);
    }

    private String getServerName() {
        String serverName = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        return serverName;
    }

    private List<Manga> addServerNameToCoverImage(List<Manga> mangas) {
        String serverName = getServerName();
        mangas.forEach(
                manga ->
                {
                    if (manga.getCoverImageUrl() != null) {
                        manga.setCoverImageUrl(serverName + manga.getCoverImageUrl());
                    }
                }
        );
        return mangas;
    }
}
