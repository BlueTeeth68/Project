package com.mangareader.web.rest;

import com.mangareader.domain.Manga;
import com.mangareader.service.IMangaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/manga")
@AllArgsConstructor
public class MangaResource {

    private final IMangaService mangaService;

    @GetMapping("/list")
    public ResponseEntity<List<Manga>> getAllMangaSortByLatestUpdate() throws URISyntaxException {
        List<Manga> mangaList = mangaService.getAllMangaSortByLatestUpdate();

        return ResponseEntity
                .created(new URI("/manga/list"))
                .body(mangaList);
    }

}
