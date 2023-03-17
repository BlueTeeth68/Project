package com.mangareader.web.rest;

import com.mangareader.domain.Genre;
import com.mangareader.exception.BadRequestException;
import com.mangareader.service.IGenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/genre")
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class GenreResource {

    private final IGenreService genreService;

    @GetMapping("/list")
    public ResponseEntity<List<Genre>> getAllGenre(
            @RequestParam(required = false, defaultValue = "50") String limit,
            @RequestParam(required = false, defaultValue = "1") String page
    ) {
        List<Genre> result = genreService.getAllPaginateGenreSortedByName(limit, page);
        return ResponseEntity.ok(result);
    }

    @GetMapping()
    public ResponseEntity<List<Genre>> getGenreByNameOrId(
            @RequestParam String id,
            @RequestParam String name
    ) {
        List<Genre> result = new ArrayList<>();
        if (id != null && name == null) {
            result.add(genreService.getGenreById(id));
        } else if (id == null && name != null) {
            result = genreService.getGenreByNameContaining(name);
        } else {
            throw new BadRequestException("Bad request when execute findById or findByName.");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
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
