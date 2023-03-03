package com.mangareader.web.rest;

import com.mangareader.service.util.APIUtil;
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
            @RequestParam(required = false) String limit,
            @RequestParam(required = false) String page
    ) {
        List<Genre> result;
        if (limit == null || page == null) {
            result = genreService.getAllPaginateGenreSortedByName(50, 0);
        } else {
            int limitNum = APIUtil.parseStringToInteger(limit, "Limit is not a number exception.");
            int pageNum = APIUtil.parseStringToInteger(page, "Page is not a number exception");
            int offset = limitNum * (pageNum - 1);
            result = genreService.getAllPaginateGenreSortedByName(limitNum, offset);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping()
    public ResponseEntity<List<Genre>> getGenreByNameOrId(
            @RequestParam String id,
            @RequestParam String name
    ) {
        List<Genre> result = new ArrayList<>();

        //find by id
        if (id != null && name == null) {
            Long idNum = APIUtil.parseStringToLong(id, "id is not a number exception.");
            result.add(genreService.getGenreById(idNum));
        } else if (id == null && name != null) {  //find by name
            result = genreService.getGenreByNameContaining(name);
        } else {
            throw new BadRequestException("Bad request when execute findById or findByName.");
        }
        return ResponseEntity.ok(result);
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
        Long idNum = APIUtil.parseStringToLong(id, "id is not a number exception.");
        genreService.deleteGenre(idNum);
        return ResponseEntity.noContent().build();
    }


}
