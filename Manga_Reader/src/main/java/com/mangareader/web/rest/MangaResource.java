package com.mangareader.web.rest;

import com.mangareader.domain.MangaStatus;
import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.service.IKeywordService;
import com.mangareader.service.IUserService;
import com.mangareader.service.dto.MangaDTO;
import com.mangareader.service.mapper.MangaMapper;
import com.mangareader.service.util.APIUtil;
import com.mangareader.domain.Manga;
import com.mangareader.service.IMangaService;
import com.mangareader.web.rest.vm.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/manga")
@RequiredArgsConstructor
@Slf4j
public class MangaResource {

    private final IMangaService mangaService;

    private final HttpServletRequest request;

    private final MangaMapper mangaMapper;

    private final IUserService userService;

    private final IKeywordService keywordService;

    @GetMapping("/list")
    public ResponseEntity<List<MangaDTO>> getAllPaginateManga(
            @RequestParam(required = false, defaultValue = "100") String limit,
            @RequestParam(required = false, defaultValue = "1") String page
    ) {
        List<Manga> mangas;

        int limitNum = APIUtil.parseStringToInteger(limit, "Limit is not a number exception.");
        int pageNum = APIUtil.parseStringToInteger(page, "Page is not a number exception");
        int offset = limitNum * (pageNum - 1);

        mangas = mangaService.getAllPaginateMangaOrderByLatestUpdate(limitNum, offset);

        List<MangaDTO> mangaDTOs = convertEntityToDTO(mangas);

        return ResponseEntity.ok(mangaDTOs);
    }

    @GetMapping()
    public ResponseEntity<MangaDTO> getMangaById(
            @RequestParam(defaultValue = "1") String id
    ) {
        Long idNum = APIUtil.parseStringToLong(id, "id is not a number.");
        Manga manga = mangaService.getMangaById(idNum);
        MangaDTO result = convertEntityToDTO(manga);
        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @GetMapping("/genre")
    public ResponseEntity<List<MangaDTO>> getMangaByGenre(
            @RequestParam(defaultValue = "1") String id,
            @RequestParam(defaultValue = "20") String limit,
            @RequestParam(defaultValue = "1") String page
    ) {
        Long genreIdNum = APIUtil.parseStringToLong(id, "genreId is not a number.");
        int limitNum = APIUtil.parseStringToInteger(limit, "limit is not a number.");
        int pageNum = APIUtil.parseStringToInteger(page, "page is not a number.");
        int offset = limitNum * (pageNum - 1);
        List<Manga> mangas = mangaService.getMangaByGenre(genreIdNum, limitNum, offset);
        List<MangaDTO> result = convertEntityToDTO(mangas);

        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @GetMapping("/author")
    public ResponseEntity<List<MangaDTO>> getMangaByAuthor(
            @RequestParam(defaultValue = "1") String id,
            @RequestParam(defaultValue = "20") String limit,
            @RequestParam(defaultValue = "1") String page
    ) {
        Long authorIdNum = APIUtil.parseStringToLong(id, "authorId is not a number.");
        int limitNum = APIUtil.parseStringToInteger(limit, "limit is not a number.");
        int pageNum = APIUtil.parseStringToInteger(page, "page is not a number.");
        int offset = limitNum * (pageNum - 1);
        List<Manga> mangas = mangaService.getMangaByAuthor(authorIdNum, limitNum, offset);
        List<MangaDTO> result = convertEntityToDTO(mangas);
        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @GetMapping("/name")
    public ResponseEntity<List<MangaDTO>> getMangasByNameOrKeywordOrderByNameWithPagination(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "20") String limit,
            @RequestParam(defaultValue = "1") String page
    ) {
        int limitNum = APIUtil.parseStringToInteger(limit, "limit is not a number.");
        int pageNum = APIUtil.parseStringToInteger(page, "page is not a number.");
        int offset = limitNum * (pageNum - 1);
        List<Manga> mangas = mangaService.getMangaByNameOrKeyword(keyword, limitNum, offset);
        List<MangaDTO> result = convertEntityToDTO(mangas);
        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @GetMapping("/translator")
    public ResponseEntity<List<MangaDTO>> getMangaByTranslator(
            @RequestParam(defaultValue = "1") String id,
            @RequestParam(defaultValue = "20") String limit,
            @RequestParam(defaultValue = "1") String page
    ) {
        Long translatorId = APIUtil.parseStringToLong(id, "translatorId is not a number.");
        int limitNum = APIUtil.parseStringToInteger(limit, "limit is not a number.");
        int pageNum = APIUtil.parseStringToInteger(page, "page is not a number.");
        int offset = limitNum * (pageNum - 1);
        List<Manga> mangas = mangaService.getMangaByTranslator(translatorId, limitNum, offset);
        List<MangaDTO> result = convertEntityToDTO(mangas);
        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @GetMapping("/suggest")
    public ResponseEntity<List<MangaDTO>> getSuggestMangas(
            @RequestParam(defaultValue = "20") String limit,
            @RequestParam(defaultValue = "1") String page
    ) {
        int limitNum = APIUtil.parseStringToInteger(limit, "limit is not a number.");
        int pageNum = APIUtil.parseStringToInteger(page, "page is not a number.");
        int offset = limitNum * (pageNum - 1);
        List<Manga> mangas = mangaService.getSuggestMangas(limitNum, offset);
        List<MangaDTO> result = convertEntityToDTO(mangas);
        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @GetMapping("/status")
    public ResponseEntity<List<MangaDTO>> getMangasByStatus(
            @RequestParam String status,
            @RequestParam(defaultValue = "20") String limit,
            @RequestParam(defaultValue = "1") String page
    ) {
        MangaStatus mangaStatus = APIUtil.parseStringToMangaStatus(status, "Status must be Ongoing or Completed");
        int limitNum = APIUtil.parseStringToInteger(limit, "limit is not a number.");
        int pageNum = APIUtil.parseStringToInteger(page, "page is not a number.");
        int offset = limitNum * (pageNum - 1);
        List<Manga> mangas = mangaService.getMangasByStatusLimit(mangaStatus.toString(), limitNum, offset);
        List<MangaDTO> result = convertEntityToDTO(mangas);
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
        manga.setUser(getCurrentUser());
        Manga result = mangaService.createManga(manga);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PatchMapping("/genre")
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<Manga> addGenreToManga(
            @Valid @RequestBody SetGenreToMangaVM vm
    ) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() == RoleName.TRANSLATOR && !checkIdentityForUpdateManga(vm.getId())) {
            log.error("Access denied.");
            throw new AccessDeniedException("Only owner can update this manga.");
        }
        Manga manga = mangaService.addGenreToManga(vm.getId(), vm.getGenreName());
        if (manga.getCoverImageUrl() != null) {
            manga.setCoverImageUrl(getServerName() + manga.getCoverImageUrl());
        }
        return new ResponseEntity<>(manga, HttpStatus.OK);
    }

    @PatchMapping("/author")
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<Manga> addAuthorToManga(
            @Valid @RequestBody SetAuthorsToMangaVM vm
    ) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() == RoleName.TRANSLATOR && !checkIdentityForUpdateManga(vm.getId())) {
            log.error("Access denied.");
            throw new AccessDeniedException("Only owner can update this manga.");
        }
        Manga manga = mangaService.addAuthorsToManga(vm.getId(), vm.getAuthorIds());
        if (manga.getCoverImageUrl() != null) {
            manga.setCoverImageUrl(getServerName() + manga.getCoverImageUrl());
        }
        return new ResponseEntity<>(manga, HttpStatus.OK);
    }


    @PatchMapping("/cover-image")
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<Manga> changeCoverImage(
            @RequestParam String id,
            @RequestParam("file") MultipartFile file
    ) {
        Long idNum = APIUtil.parseStringToLong(id, "id is not a number");
        User currentUser = getCurrentUser();
        if (currentUser.getRole() == RoleName.TRANSLATOR && !checkIdentityForUpdateManga(idNum)) {
            log.error("Access denied.");
            throw new AccessDeniedException("Only owner can update this manga.");
        }

        Manga manga = mangaService.updateCoverImage(idNum, file);
        if (manga.getCoverImageUrl() != null) {
            manga.setCoverImageUrl(getServerName() + manga.getCoverImageUrl());
        }
        return new ResponseEntity<>(manga, HttpStatus.CREATED);
    }

    @PatchMapping("/keyword")
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<Manga> addKeywordToManga(
            @Valid @RequestBody KeywordMangaVM vm
    ) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() == RoleName.TRANSLATOR && !checkIdentityForUpdateManga(vm.getMangaId())) {
            log.error("Access denied.");
            throw new AccessDeniedException("Only owner can update this manga.");
        }

        keywordService.deleteKeywordOfManga(vm.getMangaId());
        Manga manga = keywordService.addKeywordToManga(vm.getMangaId(), vm.getKeywords());
        if (manga.getCoverImageUrl() != null) {
            manga.setCoverImageUrl(getServerName() + manga.getCoverImageUrl());
        }
        return new ResponseEntity<>(manga, HttpStatus.OK);
    }

    private String getServerName() {
        String serverName = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        return serverName;
    }

    private List<Manga> addServerNameToCoverImage(List<Manga> mangas) {
        String serverName = getServerName();
        mangas.forEach(
                manga ->
                {
                    if (manga.getCoverImageUrl() != null) {
                        manga.setCoverImageUrl(serverName + manga.getCoverImageUrl());
                    }
                }
        );
        return mangas;
    }

    private List<MangaDTO> addServerNameToCoverImageDTO(List<MangaDTO> mangas) {
        String serverName = getServerName();
        mangas.forEach(
                manga ->
                {
                    if (manga.getCoverImageUrl() != null) {
                        manga.setCoverImageUrl(serverName + manga.getCoverImageUrl());
                    }
                }
        );
        return mangas;
    }

    private List<MangaDTO> convertEntityToDTO(List<Manga> mangas) {
        List<MangaDTO> result = new ArrayList<>();
        String serverName = getServerName();
        mangas.forEach(manga -> {
            result.add(mangaMapper.toDTO(manga, serverName));
        });
        return result;
    }

    private MangaDTO convertEntityToDTO(Manga manga) {
        String serverName = getServerName();
        MangaDTO result = mangaMapper.toDTO(manga, serverName);
        return result;
    }

    private User getCurrentUser() {
        log.debug("Get current user from Security Context Holder....");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.getUserByUsername(username);
        return user;
    }

    private boolean checkIdentityForUpdateManga(Long mangaId) {
        User user = getCurrentUser();
        Manga manga = mangaService.getMangaById(mangaId);
        return (user.getId() == manga.getUser().getId());
    }
}
