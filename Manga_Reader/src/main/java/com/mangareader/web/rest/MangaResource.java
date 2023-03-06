package com.mangareader.web.rest;

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

        String serverName = getServerName();

        mangas.forEach(
                manga ->
                {
                    if (manga.getCoverImageUrl() != null) {
                        manga.setCoverImageUrl(serverName + manga.getCoverImageUrl());
                    }
                }
        );

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

        return new ResponseEntity<>(mangas, HttpStatus.FOUND);
    }


    private String getServerName() {
        String serverName = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        return serverName;
    }

}
