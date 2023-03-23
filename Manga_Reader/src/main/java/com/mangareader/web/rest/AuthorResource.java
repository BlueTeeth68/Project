package com.mangareader.web.rest;

import com.mangareader.domain.Author;
import com.mangareader.service.IAuthorService;
import com.mangareader.service.dto.PagingReturnDTO;
import com.mangareader.service.util.APIUtil;
import com.mangareader.web.rest.vm.ChangeAuthorVM;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/author")
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused")
@SecurityRequirement(name = "authorize")
public class AuthorResource {
    private final IAuthorService authorService;
    private final HttpServletRequest request;

    @GetMapping("/list")
    public ResponseEntity<PagingReturnDTO<Author>> getLimitAuthor(
            @RequestParam(defaultValue = "50") String size,
            @RequestParam(defaultValue = "0") String page
    ) {
        Page<Author> authors = authorService.getLimitAuthor(size, page);
        PagingReturnDTO<Author> result = new PagingReturnDTO<>();
        result.setContent(authorService.setAvatarUrlToUser(authors.getContent(), APIUtil.getServerName(request)));
        result.setTotalElements(authors.getTotalElements());
        result.setTotalPages(authors.getTotalPages());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAuthorByIdOrName(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name
    ) {
        List<Author> result = authorService.getAuthorByIdOrName(id, name);
        result = authorService.setAvatarUrlToUser(result, APIUtil.getServerName(request));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/created-by")
    public ResponseEntity<List<Author>> getAuthorByUserId(
            @RequestParam String userId
    ) {
        List<Author> result = authorService.getAuthorByCreatedUser(userId);
        result = authorService.setAvatarUrlToUser(result, APIUtil.getServerName(request));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping

    public ResponseEntity<Author> createNewAuthor(
            @RequestBody String name
    ) {
        Author author = authorService.createAuthor(name);
        author = authorService.setAvatarUrlToUser(author, APIUtil.getServerName(request));
        return new ResponseEntity<>(author, HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<Author> changeAuthorName(
            @Valid @RequestBody ChangeAuthorVM vm
    ) {
        Author author = authorService.changeAuthorName(vm.getId(), vm.getAuthorName());
        author = authorService.setAvatarUrlToUser(author, APIUtil.getServerName(request));
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAuthor(
            @RequestParam String id
    ) {
        authorService.deleteAuthor(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
