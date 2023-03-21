package com.mangareader.web.rest;

import com.mangareader.domain.Manga;
import com.mangareader.service.IKeywordService;
import com.mangareader.service.IMangaService;
import com.mangareader.service.IUserService;
import com.mangareader.service.dto.MangaDTO;
import com.mangareader.service.dto.PagingReturnDTO;
import com.mangareader.service.mapper.MangaMapper;
import com.mangareader.service.util.APIUtil;
import com.mangareader.web.rest.vm.CreateMangaVM;
import com.mangareader.web.rest.vm.KeywordMangaVM;
import com.mangareader.web.rest.vm.SetAuthorsToMangaVM;
import com.mangareader.web.rest.vm.SetGenreToMangaVM;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/manga")
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused")
public class MangaResource {

    private final IMangaService mangaService;
    private final HttpServletRequest request;
    private final MangaMapper mangaMapper;
    private final IUserService userService;
    private final IKeywordService keywordService;

    @GetMapping("/list")
    public ResponseEntity<PagingReturnDTO<MangaDTO>> getAllPageableManga(
            @RequestParam(required = false, defaultValue = "100") String size,
            @RequestParam(required = false, defaultValue = "1") String page
    ) {
        Page<Manga> mangas = mangaService.getAllPageableMangaOrderByLatestUpdate(page, size);
        String serverName = APIUtil.getServerName(request);
        PagingReturnDTO<MangaDTO> result = mangaMapper.toPagingReturnDTOMangaDTO(mangas, serverName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<MangaDTO> getMangaById(
            @RequestParam(defaultValue = "1") String id
    ) {
        Manga manga = mangaService.getMangaById(id);
        MangaDTO result = mangaMapper.toDTO(manga, APIUtil.getServerName(request));
        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @GetMapping("/genre")
    public ResponseEntity<PagingReturnDTO<MangaDTO>> getPageableMangaByGenre(
            @RequestParam(defaultValue = "1") String id,
            @RequestParam(defaultValue = "20") String size,
            @RequestParam(defaultValue = "1") String page
    ) {
        Page<Manga> mangas = mangaService.getPageableMangaByGenre(id, page, size);
        String serverName = APIUtil.getServerName(request);
        PagingReturnDTO<MangaDTO> result = mangaMapper.toPagingReturnDTOMangaDTO(mangas, serverName);
        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @GetMapping("/author")
    public ResponseEntity<PagingReturnDTO<MangaDTO>> getPageableMangaByAuthor(
            @RequestParam(defaultValue = "1") String id,
            @RequestParam(defaultValue = "20") String size,
            @RequestParam(defaultValue = "1") String page
    ) {
        Page<Manga> mangas = mangaService.getPageableMangaByAuthor(id, page, size);
        String serverName = APIUtil.getServerName(request);
        PagingReturnDTO<MangaDTO> result = mangaMapper.toPagingReturnDTOMangaDTO(mangas, serverName);
        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @GetMapping("/name")
    public ResponseEntity<PagingReturnDTO<MangaDTO>> getPageableMangasByNameOrKeywordOrderByName(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "20") String size,
            @RequestParam(defaultValue = "1") String page
    ) {
        Page<Manga> mangas = mangaService.getPageableMangaByNameOrKeyword(keyword, page, size);
        String serverName = APIUtil.getServerName(request);
        PagingReturnDTO<MangaDTO> result = mangaMapper.toPagingReturnDTOMangaDTO(mangas, serverName);
        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @GetMapping("/translator")
    public ResponseEntity<PagingReturnDTO<MangaDTO>> getPageableMangaByTranslator(
            @RequestParam(defaultValue = "1") String id,
            @RequestParam(defaultValue = "20") String size,
            @RequestParam(defaultValue = "1") String page
    ) {
        Page<Manga> mangas = mangaService.getPageableMangaByTranslator(id, page, size);
        String serverName = APIUtil.getServerName(request);
        PagingReturnDTO<MangaDTO> result = mangaMapper.toPagingReturnDTOMangaDTO(mangas, serverName);
        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @GetMapping("/suggest")
    public ResponseEntity<PagingReturnDTO<MangaDTO>> getPageableSuggestMangas(
            @RequestParam(defaultValue = "20") String size,
            @RequestParam(defaultValue = "1") String page
    ) {
        Page<Manga> mangas = mangaService.getPageableSuggestManga(page, size);
        String serverName = APIUtil.getServerName(request);
        PagingReturnDTO<MangaDTO> result = mangaMapper.toPagingReturnDTOMangaDTO(mangas, serverName);
        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @GetMapping("/status")
    public ResponseEntity<PagingReturnDTO<MangaDTO>> getMangasByStatus(
            @RequestParam String status,
            @RequestParam(defaultValue = "20") String size,
            @RequestParam(defaultValue = "1") String page
    ) {
        Page<Manga> mangas = mangaService.getPageableMangaByStatus(status, page, size);
        String serverName = APIUtil.getServerName(request);
        PagingReturnDTO<MangaDTO> result = mangaMapper.toPagingReturnDTOMangaDTO(mangas, serverName);
        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<Manga> createNewManga(
            @Valid @RequestBody CreateMangaVM vm
    ) {
        Manga manga = new Manga();
        manga.setName(vm.getName());
        manga.setSummary(vm.getSummary());
        manga.setYearOfPublication(vm.getYearOfPublication());
        manga.setUser(userService.getCurrentUser());
        Manga result = mangaService.createManga(manga);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PatchMapping("/genre")
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<Manga> addGenreToManga(
            @Valid @RequestBody SetGenreToMangaVM vm
    ) {
        String serverName = APIUtil.getServerName(request);
        Manga manga = mangaService.addGenreToManga(vm.getId(), vm.getGenreName(), serverName);
        return new ResponseEntity<>(manga, HttpStatus.OK);
    }

    @PatchMapping("/author")
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<Manga> addAuthorToManga(
            @Valid @RequestBody SetAuthorsToMangaVM vm
    ) {
        String serverName = APIUtil.getServerName(request);
        Manga manga = mangaService.addAuthorsToManga(vm.getId(), vm.getAuthorIds(), serverName);
        return new ResponseEntity<>(manga, HttpStatus.OK);
    }

    @PatchMapping("/cover-image")
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<Manga> changeCoverImage(
            @RequestParam String id,
            @RequestParam("file") MultipartFile file
    ) {
        String serverName = APIUtil.getServerName(request);
        Manga manga = mangaService.updateCoverImage(id, file, serverName);
        return new ResponseEntity<>(manga, HttpStatus.CREATED);
    }

    @PatchMapping("/keyword")
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<Manga> addKeywordToManga(
            @Valid @RequestBody KeywordMangaVM vm
    ) {
        String serverName = APIUtil.getServerName(request);
        Manga manga = keywordService.addKeywordToManga(vm.getMangaId(), vm.getKeywords(), serverName);
        return new ResponseEntity<>(manga, HttpStatus.OK);
    }
}
