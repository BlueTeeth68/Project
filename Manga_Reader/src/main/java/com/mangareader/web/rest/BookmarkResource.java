package com.mangareader.web.rest;

import com.mangareader.domain.Bookmark;
import com.mangareader.service.IBookmarkService;
import com.mangareader.service.dto.BookmarkDTO;
import com.mangareader.service.mapper.BookmarkMapper;
import com.mangareader.service.util.APIUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookmark")
@RequiredArgsConstructor
@SecurityRequirement(name = "authorize")
@SuppressWarnings("unused")

@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Query successfully"),
        @ApiResponse(responseCode = "201", description = "Created successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request for input parameters", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized, missing or invalid JWT", content = @Content),
        @ApiResponse(responseCode = "403", description = "Access denied, do not have permission to access this resource", content = @Content),
})
@Tag(name = "10. Bookmark")

public class BookmarkResource {

    private final IBookmarkService bookmarkService;
    private final BookmarkMapper bookmarkMapper;
    private final HttpServletRequest request;

    @Operation(
            summary = "Get list bookmark",
            description = "Logged in user can get their bookmark list.")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookmarkDTO>> getAllBookmarkOfCurrentUser() {
        List<Bookmark> bookmarks = bookmarkService.getBookmarksOfCurrentUser();
        List<BookmarkDTO> bookmarkDTOs = bookmarkMapper.toListBookmarkDTO(bookmarks, APIUtil.getServerName(request));
        return new ResponseEntity<>(bookmarkDTOs, HttpStatus.OK);
    }

    @Operation(
            summary = "Get list author",
            description = "Logged in user can add manga to bookmark or redo it.")
    @PostMapping()
    public ResponseEntity<?> createBookmark(
            @RequestParam long mangaId
    ) {
        bookmarkService.createBookmark(mangaId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
