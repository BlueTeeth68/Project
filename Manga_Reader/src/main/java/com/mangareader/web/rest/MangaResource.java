package com.mangareader.web.rest;

import com.mangareader.service.util.APIUtil;
import com.mangareader.domain.Manga;
import com.mangareader.service.IMangaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
            @RequestParam(required = false) String limit,
            @RequestParam(required = false) String page
    ) {
        List<Manga> mangas;

        if (limit == null || page == null) {
            mangas = mangaService.getAllPaginateMangaOrderByLatestUpdate(1000, 0);
//            mangas = mangaService.getAllMangaSortByLatestUpdate();
        } else {

            int limitNum = APIUtil.parseStringToInteger(limit, "Limit is not a number exception.");
            int pageNum = APIUtil.parseStringToInteger(page, "Page is not a number exception");
            int offset = limitNum * (pageNum - 1);

            mangas = mangaService.getAllPaginateMangaOrderByLatestUpdate(limitNum, offset);
        }
        mangas.forEach(
                manga -> {
                    if (manga.getCoverImageUrl() != null) {
                        manga.setCoverImageUrl(getServerName() + manga.getCoverImageUrl());
                    }
                }
        );


        return ResponseEntity.ok(mangas);
    }

    private String getServerName() {
        String serverName = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        return serverName;
    }

}
