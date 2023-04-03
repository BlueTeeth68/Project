package com.mangareader.web.rest;

import com.mangareader.domain.Author;
import com.mangareader.service.IAuthorService;
import com.mangareader.service.dto.PagingReturnDTO;
import com.mangareader.service.util.APIUtil;
import com.mangareader.web.rest.vm.ChangeAuthorVM;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/author")
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused")

@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Query successfully"),
        @ApiResponse(responseCode = "201", description = "Created successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request for input parameters", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized, missing or invalid JWT", content = @Content),
        @ApiResponse(responseCode = "403", description = "Access denied, do not have permission to access this resource", content = @Content),
})
@Tag(name = "08. Author")
public class AuthorResource {
    private final IAuthorService authorService;
    private final HttpServletRequest request;

    @Operation(
            summary = "Get list author",
            description = "Admin or translator user can get list author " +
                    "from the database.",
            security = @SecurityRequirement(name = "authorize"))
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @Operation(
            summary = "Get author by id or name",
            description = "Admin or translator user can get author by " +
                    "name or id.",
            security = @SecurityRequirement(name = "authorize"))
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Author>> getAuthorByIdOrName(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name
    ) {
        List<Author> result = authorService.getAuthorByIdOrName(id, name);
        result = authorService.setAvatarUrlToUser(result, APIUtil.getServerName(request));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(
            summary = "Get author by created user",
            description = "Admin or translator user can get author by " +
                    "created user's id.",
            security = @SecurityRequirement(name = "authorize"))
    @GetMapping(value = "/created-by", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Author>> getAuthorByUserId(
            @RequestParam String userId
    ) {
        List<Author> result = authorService.getAuthorByCreatedUser(userId);
        result = authorService.setAvatarUrlToUser(result, APIUtil.getServerName(request));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(
            summary = "Create author",
            description = "Admin or translator user can create a new author.",
            security = @SecurityRequirement(name = "authorize"))
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> createNewAuthor(
            @RequestParam String name
    ) {
        Author author = authorService.createAuthor(name);
        author = authorService.setAvatarUrlToUser(author, APIUtil.getServerName(request));
        return new ResponseEntity<>(author, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Change author's name",
            description = "Admin or translator user can change their author name." +
                    " Admin user can change author's name of other user.",
            security = @SecurityRequirement(name = "authorize"))
    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> changeAuthorName(
            @Valid @RequestBody ChangeAuthorVM vm
    ) {
        Author author = authorService.changeAuthorName(vm.getId(), vm.getAuthorName());
        author = authorService.setAvatarUrlToUser(author, APIUtil.getServerName(request));
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete author by id",
            description = "Admin or translator user can delete " +
                    "their author by id. Admin user can delete others' author.",
            security = @SecurityRequirement(name = "authorize"))
    @DeleteMapping
    public ResponseEntity<?> deleteAuthor(
            @RequestParam String id
    ) {
        authorService.deleteAuthor(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
