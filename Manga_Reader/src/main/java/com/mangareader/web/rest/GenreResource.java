package com.mangareader.web.rest;

import com.mangareader.domain.Genre;
import com.mangareader.service.IGenreService;
import com.mangareader.service.dto.PagingReturnDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Query successfully"),
        @ApiResponse(responseCode = "201", description = "Created successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request for input parameters", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized, missing or invalid JWT", content = @Content),
        @ApiResponse(responseCode = "403", description = "Access denied, do not have permission to access this resource", content = @Content),
})
public class GenreResource {

    private final IGenreService genreService;

    @Operation(
            summary = "Get list of genre",
            description = "Any user can get list of genre.", tags = "Genre",
            security = @SecurityRequirement(name = "authorize", scopes = "read"))
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagingReturnDTO<Genre>> getAllGenre(
            @RequestParam(required = false, defaultValue = "50") String size,
            @RequestParam(required = false, defaultValue = "0") String page
    ) {

        Page<Genre> genres = genreService.getAllGenreByPagingAndSortByName(page, size);
        PagingReturnDTO<Genre> results = new PagingReturnDTO<>();
        results.setContent(genres.getContent());
        results.setTotalElements(genres.getTotalElements());
        results.setTotalPages(genres.getTotalPages());

        return ResponseEntity.ok(results);
    }

    @Operation(
            summary = "Get genre by id",
            description = "Any user can get genre by id.", tags = "Genre",
            security = @SecurityRequirement(name = "authorize", scopes = "read"))
    @GetMapping(value = "/id", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Genre> getGenreById(
            @RequestParam String id
    ) {
        Genre genre = genreService.getGenreById(id);
        return new ResponseEntity<>(genre, HttpStatus.OK);
    }

    @Operation(
            summary = "Get list of genre by name",
            description = "Any user can search genre by their name.", tags = "Genre",
            security = @SecurityRequirement(name = "authorize", scopes = "read"))
    @GetMapping(value = "/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Genre>> getGenreByName(
            @RequestParam String name
    ) {
        List<Genre> genres = genreService.getGenreByNameContaining(name);
        return new ResponseEntity<>(genres, HttpStatus.OK);
    }

    @Operation(
            summary = "Create new genre",
            description = "Admin user can create new genre.", tags = "Genre",
            security = @SecurityRequirement(name = "authorize"))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Genre> createGenre(
            @Valid @RequestBody Genre genre
    ) {
        Genre result = genreService.createNewGenre(genre.getName());
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Change genre name",
            description = "Admin user can change genre name.", tags = "Genre",
            security = @SecurityRequirement(name = "authorize"))
    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Genre> changeGenreName(
            @Valid @RequestBody Genre genre
    ) {
        Genre result = genreService.changeGenreName(genre);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Delete genre by id",
            description = "Admin user can delete a genre.", tags = "Genre",
            security = @SecurityRequirement(name = "authorize"))
    @DeleteMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteGenre(
            @RequestParam String id
    ) {
        genreService.deleteGenre(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
