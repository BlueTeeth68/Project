package com.mangareader.web.rest;

import com.mangareader.domain.Manga;
import com.mangareader.domain.Rate;
import com.mangareader.service.IKeywordService;
import com.mangareader.service.IMangaService;
import com.mangareader.service.IRateService;
import com.mangareader.service.IUserService;
import com.mangareader.service.dto.CommonMangaDTO;
import com.mangareader.service.dto.MangaDTO;
import com.mangareader.service.dto.PagingReturnDTO;
import com.mangareader.service.mapper.MangaMapper;
import com.mangareader.service.util.APIUtil;
import com.mangareader.web.rest.vm.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/manga")
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

@Tag(name = "04. Manga")
public class MangaResource {

    private final IMangaService mangaService;
    private final HttpServletRequest request;
    private final MangaMapper mangaMapper;
    private final IUserService userService;
    private final IKeywordService keywordService;
    private final IRateService rateService;

    @Operation(
            summary = "Get all manga",
            description = "User can get all manga from data base with pageable. The result is " +
                    "sorted by latest update desc.",
            security = @SecurityRequirement(name = "authorize", scopes = "read"))
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagingReturnDTO<CommonMangaDTO>> getAllPageableManga(
            @RequestParam(required = false, defaultValue = "100") String size,
            @RequestParam(required = false, defaultValue = "0") String page
    ) {
        Page<Manga> mangas = mangaService.getAllPageableMangaOrderByLatestUpdate(page, size);
        String serverName = APIUtil.getServerName(request);
        PagingReturnDTO<CommonMangaDTO> result = mangaMapper.toPagingReturnDTOSearchMangaDTO(mangas, serverName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(
            summary = "Get manga by id",
            description = "User can get manga by its id",
            security = @SecurityRequirement(name = "authorize", scopes = "read"))
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MangaDTO> getMangaById(
            @RequestParam(defaultValue = "1") String id
    ) {
        Manga manga = mangaService.getMangaById(id);
        MangaDTO result = mangaMapper.toDTO(manga, APIUtil.getServerName(request));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(
            summary = "Get manga by genre",
            description = "User can get manga by its genre with pageable. The result is " +
                    "sorted by name asc",
            security = @SecurityRequirement(name = "authorize", scopes = "read"))
    @GetMapping(value = "/genre", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagingReturnDTO<CommonMangaDTO>> getPageableMangaByGenre(
            @RequestParam(defaultValue = "1") String genreId,
            @RequestParam(defaultValue = "20") String size,
            @RequestParam(defaultValue = "0") String page
    ) {
        Page<Manga> mangas = mangaService.getPageableMangaByGenre(genreId, page, size);
        String serverName = APIUtil.getServerName(request);
        PagingReturnDTO<CommonMangaDTO> result = mangaMapper.toPagingReturnDTOSearchMangaDTO(mangas, serverName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(
            summary = "Get mangas by author",
            description = "User can get mangas by its author with pageable. The result is " +
                    "sorted by name asc.",
            security = @SecurityRequirement(name = "authorize", scopes = "read"))
    @GetMapping(value = "/author", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagingReturnDTO<CommonMangaDTO>> getPageableMangaByAuthor(
            @RequestParam(defaultValue = "1") String authorId,
            @RequestParam(defaultValue = "20") String size,
            @RequestParam(defaultValue = "0") String page
    ) {
        Page<Manga> mangas = mangaService.getPageableMangaByAuthor(authorId, page, size);
        String serverName = APIUtil.getServerName(request);
        PagingReturnDTO<CommonMangaDTO> result = mangaMapper.toPagingReturnDTOSearchMangaDTO(mangas, serverName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(
            summary = "Get mangas by name or keyword",
            description = "User can get mangas by its name or keyword with pageable. The result is " +
                    "sorted by name asc.",
            security = @SecurityRequirement(name = "authorize", scopes = "read"))
    @GetMapping(value = "/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagingReturnDTO<CommonMangaDTO>> getPageableMangasByNameOrKeywordOrderByName(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "20") String size,
            @RequestParam(defaultValue = "0") String page
    ) {
        Page<Manga> mangas = mangaService.getPageableMangaByNameOrKeyword(keyword, page, size);
        String serverName = APIUtil.getServerName(request);
        PagingReturnDTO<CommonMangaDTO> result = mangaMapper.toPagingReturnDTOSearchMangaDTO(mangas, serverName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(
            summary = "Get mangas by translator",
            description = "Get mangas by its translator with pageable. The result is " +
                    "sorted by name.",
            security = @SecurityRequirement(name = "authorize", scopes = "read"))
    @GetMapping(value = "/translator", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagingReturnDTO<CommonMangaDTO>> getPageableMangaByTranslator(
            @RequestParam(defaultValue = "1") String translatorId,
            @RequestParam(defaultValue = "20") String size,
            @RequestParam(defaultValue = "0") String page
    ) {
        Page<Manga> mangas = mangaService.getPageableMangaByTranslator(translatorId, page, size);
        String serverName = APIUtil.getServerName(request);
        PagingReturnDTO<CommonMangaDTO> result = mangaMapper.toPagingReturnDTOSearchMangaDTO(mangas, serverName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(
            summary = "Get suggest manga",
            description = "User can get list suggest manga with pageable. The manga is sorted by " +
                    "view * rate.",
            security = @SecurityRequirement(name = "authorize", scopes = "read"))
    @GetMapping(value = "/suggest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagingReturnDTO<CommonMangaDTO>> getPageableSuggestMangas(
            @RequestParam(defaultValue = "20") String size,
            @RequestParam(defaultValue = "0") String page
    ) {
        Page<Manga> mangas = mangaService.getPageableSuggestManga(page, size);
        String serverName = APIUtil.getServerName(request);
        PagingReturnDTO<CommonMangaDTO> result = mangaMapper.toPagingReturnDTOSearchMangaDTO(mangas, serverName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(
            summary = "Get manga by status",
            description = "User can get manga by its status with pageable. The status can be Ongoing " +
                    "or Completed.",
            security = @SecurityRequirement(name = "authorize", scopes = "read"))
    @GetMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagingReturnDTO<CommonMangaDTO>> getMangasByStatus(
            @RequestParam String status,
            @RequestParam(defaultValue = "20") String size,
            @RequestParam(defaultValue = "0") String page
    ) {
        Page<Manga> mangas = mangaService.getPageableMangaByStatus(status, page, size);
        String serverName = APIUtil.getServerName(request);
        PagingReturnDTO<CommonMangaDTO> result = mangaMapper.toPagingReturnDTOSearchMangaDTO(mangas, serverName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(
            summary = "Create new manga",
            description = "Admin and Translator user can create a new manga.",
            security = @SecurityRequirement(name = "authorize"))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<MangaDTO> createNewManga(
            @Valid @RequestBody CreateMangaVM vm
    ) {
        Manga manga = new Manga();
        manga.setUser(userService.getCurrentUser());
        manga.setName(vm.getName());
        manga.setSummary(vm.getSummary());
        manga.setYearOfPublication(vm.getYearOfPublication());
        manga.setUser(userService.getCurrentUser());
        manga = mangaService.createManga(manga);
        MangaDTO mangaDTO = mangaMapper.toDTO(manga, APIUtil.getServerName(request));
        return new ResponseEntity<>(mangaDTO, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Change manga information",
            description = "Admin or Translator user can change their own manga's information. Admin user" +
                    " can change any manga's information.",
            security = @SecurityRequirement(name = "authorize"))
    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<MangaDTO> changeMangaInformation(
            @Valid @RequestBody ChangeMangaVM vm
    ) {
        Manga manga = mangaService.changeMangaInformation(vm);
        MangaDTO mangaDTO = mangaMapper.toDTO(manga, APIUtil.getServerName(request));
        return new ResponseEntity<>(mangaDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Set genre to manga",
            description = "Admin or Translator user can set genre to their manga.",
            security = @SecurityRequirement(name = "authorize"))
    @PatchMapping(value = "/genre", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<MangaDTO> addGenreToManga(
            @Valid @RequestBody SetGenreToMangaVM vm
    ) {
        String serverName = APIUtil.getServerName(request);
        Manga manga = mangaService.addGenreToManga(vm.getMangaId(), vm.getGenreName());
        MangaDTO mangaDTO = mangaMapper.toDTO(manga, serverName);
        return new ResponseEntity<>(mangaDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Add author to manga",
            description = "Admin or Translator user can set author list to their manga.",
            security = @SecurityRequirement(name = "authorize"))
    @PatchMapping(value = "/author", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")

    public ResponseEntity<MangaDTO> addAuthorToManga(
            @Valid @RequestBody SetAuthorsToMangaVM vm
    ) {
        String serverName = APIUtil.getServerName(request);
        Manga manga = mangaService.addAuthorsToManga(vm.getMangaId(), vm.getAuthorIds());
        MangaDTO mangaDTO = mangaMapper.toDTO(manga, serverName);
        return new ResponseEntity<>(mangaDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Change manga cover image",
            description = "Admin or Translator user can change their manga's cover image.",
            security = @SecurityRequirement(name = "authorize"))
    @PatchMapping(value = "/cover-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<MangaDTO> changeCoverImage(
            @RequestParam String mangaId,
            @RequestPart("file") MultipartFile file
    ) {
        String serverName = APIUtil.getServerName(request);
        Manga manga = mangaService.updateCoverImage(mangaId, file);
        MangaDTO mangaDTO = mangaMapper.toDTO(manga, serverName);
        return new ResponseEntity<>(mangaDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Add keyword to manga",
            description = "Admin or Translator user can set list of keyword to their manga.",
            security = @SecurityRequirement(name = "authorize"))
    @PatchMapping(value = "/keyword", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<MangaDTO> addKeywordToManga(
            @Valid @RequestBody KeywordMangaVM vm
    ) {
        String serverName = APIUtil.getServerName(request);
        Manga manga = keywordService.addKeywordToManga(vm.getMangaId(), vm.getKeywords());
        MangaDTO mangaDTO = mangaMapper.toDTO(manga, serverName);
        return new ResponseEntity<>(mangaDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Rate manga",
            description = "Logged in user can rate a manga.",
            security = @SecurityRequirement(name = "authorize"))
    @PatchMapping(value = "/rate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RateVM> rateManga(
            @Valid @RequestBody RateVM vm
    ) {
        Rate rate = rateService.rateManga(vm);
        RateVM result = new RateVM();
        result.setMangaId(rate.getManga().getId());
        result.setPoint(rate.getPoint());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete manga",
            description = "Admin or Translator can delete their own manga.",
            security = @SecurityRequirement(name = "authorize"))
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<?> deleteManga(
            @PathVariable Long id
    ) {
        mangaService.deleteManga(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
