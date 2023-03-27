package com.mangareader.web.rest;

import com.mangareader.domain.Bookmark;
import com.mangareader.service.IBookmarkService;
import com.mangareader.service.dto.BookmarkDTO;
import com.mangareader.service.mapper.BookmarkMapper;
import com.mangareader.service.util.APIUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookmark")
@RequiredArgsConstructor
@SecurityRequirement(name = "authorize")
@SuppressWarnings("unused")
public class BookmarkResource {

    private final IBookmarkService bookmarkService;
    private final BookmarkMapper bookmarkMapper;
    private final HttpServletRequest request;

    @GetMapping()
    public ResponseEntity<List<BookmarkDTO>> getAllBookmarkOfCurrentUser() {
        List<Bookmark> bookmarks = bookmarkService.getBookmarksOfCurrentUser();
        List<BookmarkDTO> bookmarkDTOs = bookmarkMapper.toListBookmarkDTO(bookmarks, APIUtil.getServerName(request));
        return new ResponseEntity<>(bookmarkDTOs, HttpStatus.OK);
    }

    @PostMapping("/{mangaId}")
    public ResponseEntity<?> createBookmark(
            @PathVariable Long mangaId
    ) {
        bookmarkService.createBookmark(mangaId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
