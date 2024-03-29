package com.mangareader.web.rest;

import com.mangareader.service.IChapterService;
import com.mangareader.service.IMangaService;
import com.mangareader.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class ImageResource {

    private final IUserService userService;
    private final IMangaService mangaService;
    private final IChapterService chapterService;

    @GetMapping("/avatar/{filename:.+}")
    public ResponseEntity<Resource> loadFile(@PathVariable String filename) {

        Resource file = userService.getAvatar(filename);

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
    }

    @GetMapping("/manga/{folder}/{filename:.+}")
    public ResponseEntity<Resource> loadCoverImageFile(
            @PathVariable("filename") String filename,
            @PathVariable("folder") String folder
    ) {

        Resource file = mangaService.getCoverImage(folder + "/" + filename);

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
    }

    @GetMapping("/manga/{folder}/{chapterNumber}/{filename:.+}")
    public ResponseEntity<Resource> loadCoverImageFile(
            @PathVariable("filename") String filename,
            @PathVariable("chapterNumber") String chapterNumber,
            @PathVariable("folder") String folder
    ) {
        Resource file = mangaService.getCoverImage(folder + "/" + chapterNumber + "/" + filename);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
    }
}
