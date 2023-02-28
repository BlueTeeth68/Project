package com.mangareader.web.rest;

import com.mangareader.Util.APIUtil;
import com.mangareader.domain.Genre;
import com.mangareader.exception.BadRequestException;
import com.mangareader.service.IGenreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genre")
@EnableMethodSecurity(prePostEnabled = true)
public class GenreResource {

    @Autowired
    private IGenreService genreService;

    @GetMapping("/list")
    public ResponseEntity<List<Genre>> getAllGenre() {
        return ResponseEntity.ok(genreService.getAllGenre());
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Genre> getGenreByNameOrId(
            @RequestParam String id,
            @RequestParam String name
    ) {
        Genre genre;
        //find by id
        if (id != null && name == null) {
            Long idNum = APIUtil.parseStringToLong(id, "id is not a number exception.");
            genre = genreService.getGenreById(idNum);
        } else if (id == null && name != null) {  //find by name
            genre = genreService.getGenreByName(name);
        } else {
            throw new BadRequestException("Bad request when execute findById or findByName.");
        }
        return ResponseEntity.ok(genre);
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Genre> createGenre(
            @Valid @RequestBody Genre genre
    ) {
        Genre result = genreService.createNewGenre(genre);
        return ResponseEntity.ok(result);
    }

//    @PatchMapping()
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<Genre> changeGenreName(
//            @RequestParam String id,
//            @RequestParam String name
//    ) {
//        Long idNum = APIUtil.parseStringToLong(id, "id is not a number exception.");
//        if (name == null || name.isBlank()) {
//            throw new BadRequestException("Genre name is empty or blank.");
//        }
//        Genre result = genreService.changeGenreName(idNum, name);
//        return ResponseEntity.ok(result);
//    }


    @PatchMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Genre> changeGenreName(
            @Valid @RequestBody Genre genre
    ) {
        Genre result = genreService.changeGenreName(genre);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteGenre(
            @RequestParam String id
    ) {
        Long idNum = APIUtil.parseStringToLong(id, "id is not a number exception.");
        genreService.deleteGenre(idNum);
        return ResponseEntity.noContent().build();
    }


}
