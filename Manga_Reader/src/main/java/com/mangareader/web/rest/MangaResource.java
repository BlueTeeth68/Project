package com.mangareader.web.rest;

import com.mangareader.domain.Manga;
import com.mangareader.domain.Rate;
import com.mangareader.service.IKeywordService;
import com.mangareader.service.IMangaService;
import com.mangareader.service.IRateService;
import com.mangareader.service.IUserService;
import com.mangareader.service.dto.MangaDTO;
import com.mangareader.service.dto.PagingReturnDTO;
import com.mangareader.service.dto.CommonMangaDTO;
import com.mangareader.service.mapper.MangaMapper;
import com.mangareader.service.util.APIUtil;
import com.mangareader.web.rest.vm.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
//@SecurityRequirement(name = "authorize")
public class MangaResource {

    private final IMangaService mangaService;
    private final HttpServletRequest request;
    private final MangaMapper mangaMapper;
    private final IUserService userService;
    private final IKeywordService keywordService;
    private final IRateService rateService;

    @GetMapping("/list")
    public ResponseEntity<PagingReturnDTO<CommonMangaDTO>> getAllPageableManga(
            @RequestParam(required = false, defaultValue = "100") String size,
            @RequestParam(required = false, defaultValue = "0") String page
    ) {
        Page<Manga> mangas = mangaService.getAllPageableMangaOrderByLatestUpdate(page, size);
        String serverName = APIUtil.getServerName(request);
        PagingReturnDTO<CommonMangaDTO> result = mangaMapper.toPagingReturnDTOSearchMangaDTO(mangas, serverName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<MangaDTO> getMangaById(
            @RequestParam(defaultValue = "1") String id
    ) {
        Manga manga = mangaService.getMangaById(id);
        MangaDTO result = mangaMapper.toDTO(manga, APIUtil.getServerName(request));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/genre")
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

    @GetMapping("/author")
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

    @GetMapping("/name")
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

    @GetMapping("/translator")
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

    @GetMapping("/suggest")
    public ResponseEntity<PagingReturnDTO<CommonMangaDTO>> getPageableSuggestMangas(
            @RequestParam(defaultValue = "20") String size,
            @RequestParam(defaultValue = "0") String page
    ) {
        Page<Manga> mangas = mangaService.getPageableSuggestManga(page, size);
        String serverName = APIUtil.getServerName(request);
        PagingReturnDTO<CommonMangaDTO> result = mangaMapper.toPagingReturnDTOSearchMangaDTO(mangas, serverName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/status")
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

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    @SecurityRequirement(name = "authorize")
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

    @PatchMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    @SecurityRequirement(name = "authorize")
    public ResponseEntity<MangaDTO> changeMangaInformation(
            @Valid @RequestBody ChangeMangaVM vm
    ) {
        Manga manga = mangaService.changeMangaInformation(vm);
        MangaDTO mangaDTO = mangaMapper.toDTO(manga, APIUtil.getServerName(request));
        return new ResponseEntity<>(mangaDTO, HttpStatus.OK);
    }

    @PatchMapping("/genre")
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    @SecurityRequirement(name = "authorize")
    public ResponseEntity<MangaDTO> addGenreToManga(
            @Valid @RequestBody SetGenreToMangaVM vm
    ) {
        String serverName = APIUtil.getServerName(request);
        Manga manga = mangaService.addGenreToManga(vm.getMangaId(), vm.getGenreName(), serverName);
        MangaDTO mangaDTO = mangaMapper.toDTO(manga, APIUtil.getServerName(request));
        return new ResponseEntity<>(mangaDTO, HttpStatus.OK);
    }

    @PatchMapping("/author")
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    @SecurityRequirement(name = "authorize")
    public ResponseEntity<MangaDTO> addAuthorToManga(
            @Valid @RequestBody SetAuthorsToMangaVM vm
    ) {
        String serverName = APIUtil.getServerName(request);
        Manga manga = mangaService.addAuthorsToManga(vm.getMangaId(), vm.getAuthorIds(), serverName);
        MangaDTO mangaDTO = mangaMapper.toDTO(manga, APIUtil.getServerName(request));
        return new ResponseEntity<>(mangaDTO, HttpStatus.OK);
    }

    @PatchMapping("/cover-image")
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    @SecurityRequirement(name = "authorize")
    public ResponseEntity<MangaDTO> changeCoverImage(
            @RequestParam String mangaId,
            @RequestParam("file") MultipartFile file
    ) {
        String serverName = APIUtil.getServerName(request);
        Manga manga = mangaService.updateCoverImage(mangaId, file, serverName);
        MangaDTO mangaDTO = mangaMapper.toDTO(manga, APIUtil.getServerName(request));
        return new ResponseEntity<>(mangaDTO, HttpStatus.OK);
    }

    @PatchMapping("/keyword")
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    @SecurityRequirement(name = "authorize")
    public ResponseEntity<MangaDTO> addKeywordToManga(
            @Valid @RequestBody KeywordMangaVM vm
    ) {
        String serverName = APIUtil.getServerName(request);
        Manga manga = keywordService.addKeywordToManga(vm.getMangaId(), vm.getKeywords(), serverName);
        MangaDTO mangaDTO = mangaMapper.toDTO(manga, APIUtil.getServerName(request));
        return new ResponseEntity<>(mangaDTO, HttpStatus.OK);
    }

    @PatchMapping("/rate")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "authorize")
    public ResponseEntity<RateVM> rateManga(
            @Valid @RequestBody RateVM vm
    ) {
        Rate rate = rateService.rateManga(vm);
        RateVM result = new RateVM();
        result.setMangaId(rate.getManga().getId());
        result.setPoint(rate.getPoint());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    @SecurityRequirement(name = "authorize")
    public ResponseEntity<?> deleteManga(
            @PathVariable Long id
    ) {
        mangaService.deleteManga(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
