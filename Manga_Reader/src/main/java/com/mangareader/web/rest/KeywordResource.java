package com.mangareader.web.rest;

import com.mangareader.domain.Keyword;
import com.mangareader.service.IKeywordService;
import com.mangareader.service.dto.KeywordDTO;
import com.mangareader.service.mapper.KeywordMapper;
import com.mangareader.web.rest.vm.ChangeKeywordVM;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/keyword")
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Query successfully"),
        @ApiResponse(responseCode = "201", description = "Created successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request for input parameters", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized, missing or invalid JWT", content = @Content),
        @ApiResponse(responseCode = "403", description = "Access denied, do not have permission to access this resource", content = @Content),
})
@Tag(name = "09. Keyword")

public class KeywordResource {

    private final IKeywordService keywordService;
    private final KeywordMapper keywordMapper;

    @Operation(
            summary = "Get manga keywords",
            description = "Any user can get manga keywords.",
            security = @SecurityRequirement(name = "authorize", scopes = "read"))
    @GetMapping(value = "/manga", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getKeywordOfManga(
            @RequestParam long mangaId
    ) {
        List<Keyword> keywords = keywordService.getKeywordsOfMangaSortedByName(mangaId);
        List<String> result = new ArrayList<>();
        keywords.forEach(
                k -> result.add(k.getName())
        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(
            summary = "Create keyword for manga",
            description = "Admin or translator user can create a keyword of their manga.",
            security = @SecurityRequirement(name = "authorize"))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<String> addKeywordToManga(
            @Valid @RequestBody KeywordDTO keywordDTO
    ) {
        Keyword result = keywordMapper.toEntity(keywordDTO);
        result = keywordService.createKeyWord(result);
        return new ResponseEntity<>(result.getName(), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Change keyword of manga",
            description = "Admin or translator user can change keyword name of their manga.",
            security = @SecurityRequirement(name = "authorize"))
    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<String> changeKeywordName(
            @Valid @RequestBody ChangeKeywordVM vm
    ) {
        Keyword result = keywordService.changeKeywordName(vm);
        return new ResponseEntity<>(result.getName(), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete keyword of manga",
            description = "Admin or translator user can delete a keyword of their manga.",
            security = @SecurityRequirement(name = "authorize"))
    @DeleteMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<?> deleteKeyword(
            @RequestParam String name,
            @RequestParam long mangaId
    ) {
        keywordService.deleteKeyword(name, mangaId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
