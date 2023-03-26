package com.mangareader.web.rest;

import com.mangareader.domain.Chapter;
import com.mangareader.domain.Manga;
import com.mangareader.service.IChapterService;
import com.mangareader.service.IMangaService;
import com.mangareader.service.dto.ChapterImageDTO;
import com.mangareader.service.dto.MangaDTO;
import com.mangareader.service.mapper.ChapterMapper;
import com.mangareader.service.mapper.MangaMapper;
import com.mangareader.service.util.APIUtil;
import com.mangareader.web.rest.vm.ChangeChapterVM;
import com.mangareader.web.rest.vm.CreateChapterVM;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/manga/chapter")
@RequiredArgsConstructor
@EnableMethodSecurity
@SuppressWarnings("unused")
public class ChapterResource {

    private final IChapterService chapterService;
    private final IMangaService mangaService;
    private final MangaMapper mangaMapper;
    private final ChapterMapper chapterMapper;
    private final HttpServletRequest request;

    @GetMapping("/{id}")
    public ResponseEntity<ChapterImageDTO> getChapterById(
            @PathVariable Long id
    ) {
        Chapter chapter = chapterService.getChapterById(id);
        Manga manga = chapter.getManga();
        mangaService.increaseMangaView(manga.getId());
        ChapterImageDTO chapterImageDTO = chapterMapper.toChapterImageDTO(
                chapter,
                APIUtil.getServerName(request)
        );
        return new ResponseEntity<>(chapterImageDTO, HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    @SecurityRequirement(name = "authorize")
    public ResponseEntity<MangaDTO> createNewChapter(
            @Valid @RequestBody CreateChapterVM vm
    ) {
        chapterService.createChapter(vm);
        Manga manga = mangaService.getMangaById(vm.getMangaId());
        MangaDTO mangaDTO = mangaMapper.toDTO(manga, APIUtil.getServerName(request));
        return new ResponseEntity<>(mangaDTO, HttpStatus.OK);
    }

    @PostMapping("/chapter-images/{chapterId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    @SecurityRequirement(name = "authorize")
    public ResponseEntity<MangaDTO> addImagesToChapter(
            @RequestParam MultipartFile[] files,
            @PathVariable Long chapterId
    ) {
        Manga manga = chapterService.addImagesToChapter(files, chapterId);
        MangaDTO mangaDTO = mangaMapper.toDTO(manga, APIUtil.getServerName(request));
        return new ResponseEntity<>(mangaDTO, HttpStatus.CREATED);
    }

    @PatchMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    @SecurityRequirement(name = "authorize")
    public ResponseEntity<MangaDTO> updateChapterInformation(
            @Valid @RequestBody ChangeChapterVM vm
    ) {
        Manga manga = chapterService.updateChapterInformation(vm);
        MangaDTO mangaDTO = mangaMapper.toDTO(manga, APIUtil.getServerName(request));
        return new ResponseEntity<>(mangaDTO, HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    @SecurityRequirement(name = "authorize")
    public ResponseEntity<MangaDTO> deleteChapter(
            @RequestParam String id
    ) {
        Manga manga = chapterService.deleteChapter(id);
        MangaDTO mangaDTO = mangaMapper.toDTO(manga, APIUtil.getServerName(request));
        return new ResponseEntity<>(mangaDTO, HttpStatus.OK);
    }

}
