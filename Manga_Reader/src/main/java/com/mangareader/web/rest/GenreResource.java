package com.mangareader.web.rest;

import com.mangareader.domain.Genre;
import com.mangareader.service.IGenreService;
import com.mangareader.service.dto.PagingReturnDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genre")
@EnableMethodSecurity
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class GenreResource {

    private final IGenreService genreService;

    @GetMapping("/list")
    public ResponseEntity<PagingReturnDTO<Genre>> getAllGenre(
            @RequestParam(required = false, defaultValue = "50") String size,
            @RequestParam(required = false, defaultValue = "1") String page
    ) {

        Page<Genre> genres = genreService.getAllGenreByPagingAndSortByName(page, size);
        PagingReturnDTO<Genre> results = new PagingReturnDTO<>();
        results.setContent(genres.getContent());
        results.setTotalElements(genres.getTotalElements());
        results.setTotalPages(genres.getTotalPages());

        return ResponseEntity.ok(results);
    }

    @GetMapping("/id")
    public ResponseEntity<Genre> getGenreById(
            @RequestParam String id
    ) {
        Genre genre = genreService.getGenreById(id);
        return new ResponseEntity<>(genre, HttpStatus.FOUND);
    }

    @GetMapping("/name")
    public ResponseEntity<List<Genre>> getGenreByName(
            @RequestParam String name
    ) {
        List<Genre> genres = genreService.getGenreByNameContaining(name);
        return new ResponseEntity<>(genres, HttpStatus.FOUND);
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Genre> createGenre(
            @Valid @RequestBody Genre genre
    ) {
        Genre result = genreService.createNewGenre(genre);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PatchMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Genre> changeGenreName(
            @Valid @RequestBody Genre genre
    ) {
        Genre result = genreService.changeGenreName(genre);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteGenre(
            @RequestParam String id
    ) {
        genreService.deleteGenre(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
