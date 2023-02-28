package com.mangareader.web.rest;

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
public class ImageResource {

    private final IUserService userService;

    @GetMapping("/avatar/{filename:.+}")
    public ResponseEntity<Resource> loadFile(@PathVariable String filename) {

        Resource file = userService.getAvatar(filename);

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
    }
}
