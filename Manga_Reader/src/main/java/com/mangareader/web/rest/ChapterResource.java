package com.mangareader.web.rest;

import com.mangareader.domain.Chapter;
import com.mangareader.domain.Manga;
import com.mangareader.service.IChapterService;
import com.mangareader.service.IHistoryService;
import com.mangareader.service.IMangaService;
import com.mangareader.service.IUserService;
import com.mangareader.service.dto.ChapterImageDTO;
import com.mangareader.service.dto.MangaDTO;
import com.mangareader.service.mapper.ChapterMapper;
import com.mangareader.service.mapper.MangaMapper;
import com.mangareader.service.util.APIUtil;
import com.mangareader.web.rest.vm.ChangeChapterVM;
import com.mangareader.web.rest.vm.CreateChapterVM;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Query successfully"),
        @ApiResponse(responseCode = "201", description = "Created successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request for input parameters", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized, missing or invalid JWT", content = @Content),
        @ApiResponse(responseCode = "403", description = "Access denied, do not have permission to access this resource", content = @Content),
})
public class ChapterResource {

    private final IChapterService chapterService;
    private final IMangaService mangaService;
    private final MangaMapper mangaMapper;
    private final ChapterMapper chapterMapper;
    private final HttpServletRequest request;
    private final IHistoryService historyService;
    private final IUserService userService;

    @Operation(
            summary = "Get chapter by id",
            description = "Any user can get chapter by its id.", tags = "Chapter",
            security = @SecurityRequirement(name = "authorize", scopes = "read"))
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChapterImageDTO> getChapterById(
            @RequestParam long id
    ) {
        Chapter chapter = chapterService.getChapterById(id);
        Manga manga = chapter.getManga();
        //increase manga's view
        mangaService.increaseMangaView(manga.getId());
        ChapterImageDTO chapterImageDTO = chapterMapper.toChapterImageDTO(
                chapter,
                APIUtil.getServerName(request)
        );
        //update history
        if (userService.isUserLogin()) {
            historyService.createNewHistory(id);
        }
        return new ResponseEntity<>(chapterImageDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Create new chapter",
            description = "Admin or translator user cna create new chapter " +
                    "of their manga. Admin can add chapter of other's mangas. " +
                    "The latest update of manga will be update.", tags = "Chapter",
            security = @SecurityRequirement(name = "authorize"))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<MangaDTO> createNewChapter(
            @Valid @RequestBody CreateChapterVM vm
    ) {
        chapterService.createChapter(vm);
        Manga manga = mangaService.getMangaById(vm.getMangaId());
        MangaDTO mangaDTO = mangaMapper.toDTO(manga, APIUtil.getServerName(request));
        return new ResponseEntity<>(mangaDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Change chapter cover image",
            description = "Admin or translator can add chapter images to their chapter. " +
                    "Admin user can add chapter images to other's chapter.", tags = "Chapter",
            security = @SecurityRequirement(name = "authorize"))
    @PostMapping(value = "/chapter-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<MangaDTO> addImagesToChapter(
            @RequestPart MultipartFile[] files,
            @RequestParam long chapterId
    ) {
        Manga manga = chapterService.addImagesToChapter(files, chapterId);
        MangaDTO mangaDTO = mangaMapper.toDTO(manga, APIUtil.getServerName(request));
        return new ResponseEntity<>(mangaDTO, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update chapter information",
            description = "Admin or translator can update their chapter's information. " +
                    "Admin user can update other's chapter's information.", tags = "Chapter",
            security = @SecurityRequirement(name = "authorize"))
    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<MangaDTO> updateChapterInformation(
            @Valid @RequestBody ChangeChapterVM vm
    ) {
        Manga manga = chapterService.updateChapterInformation(vm);
        MangaDTO mangaDTO = mangaMapper.toDTO(manga, APIUtil.getServerName(request));
        return new ResponseEntity<>(mangaDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete chapter",
            description = "Admin or translator can delete their chapter. " +
                    "Admin user can delete other's chapter.", tags = "Chapter",
            security = @SecurityRequirement(name = "authorize"))
    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<MangaDTO> deleteChapter(
            @RequestParam String id
    ) {
        Manga manga = chapterService.deleteChapter(id);
        MangaDTO mangaDTO = mangaMapper.toDTO(manga, APIUtil.getServerName(request));
        return new ResponseEntity<>(mangaDTO, HttpStatus.OK);
    }

}
