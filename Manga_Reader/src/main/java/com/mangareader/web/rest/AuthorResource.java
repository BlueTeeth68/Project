package com.mangareader.web.rest;

import com.mangareader.domain.Author;
import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.exception.BadRequestException;
import com.mangareader.service.IAuthorService;
import com.mangareader.service.IUserService;
import com.mangareader.service.util.APIUtil;
import com.mangareader.web.rest.vm.ChangeAuthorVM;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/author")
@RequiredArgsConstructor
@Slf4j
public class AuthorResource {

    private final IAuthorService authorService;
    private final IUserService userService;
    private final HttpServletRequest request;

    @GetMapping("/list")
    public ResponseEntity<List<Author>> getLimitAuthor(
            @RequestParam(defaultValue = "50") String limit,
            @RequestParam(defaultValue = "1") String page
    ) {
        List<Author> authors;

        int limitNum = APIUtil.parseStringToInteger(limit, "Limit is not a number exception.");
        int pageNum = APIUtil.parseStringToInteger(page, "Page is not a number exception");
        int offset = limitNum * (pageNum - 1);

        authors = setAvatarUrlToUser(authorService.getLimitAuthor(limitNum, offset));

        return new ResponseEntity<>(authors, HttpStatus.FOUND);
    }

    //find author by id or name
    @GetMapping
    public ResponseEntity<List<Author>> getAuthorByIdOrName(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name
    ) {

        List<Author> result = new ArrayList<>();

        //find by id
        if (id != null && name == null) {
            Long idNum = APIUtil.parseStringToLong(id, "id is not a number exception.");
            result.add(authorService.getAuthorById(idNum));
        }
        //find by name
        else if (id == null && name != null) {
            result = authorService.getAuthorsByName(name);
        } else {
            throw new BadRequestException("Bad request for id and name value.");
        }
        result = setAvatarUrlToUser(result);

        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    //find Authors by user_id
    @GetMapping("/created-by")
    public ResponseEntity<List<Author>> getAuthorByUserId(
            @RequestParam String userId
    ) {
        Long userIdNum = APIUtil.parseStringToLong(userId, "userId is not a number exception");

        List<Author> result = setAvatarUrlToUser(authorService.getAuthorByCreatedUser(userIdNum));

        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<Author> createNewAuthor(
            @RequestBody String name
    ) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("name is null or blank");
        }
        Author author = new Author();
        author.setName(name);
        User createdBy = getCurrentUser();
        author.setUser(createdBy);
        Author result = setAvatarUrlToUser(authorService.createAuthor(author));

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    //changed here
    @PatchMapping
    public ResponseEntity<Author> changeAuthorName(
            @Valid @RequestBody ChangeAuthorVM vm
    ) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() == RoleName.TRANSLATOR) {
            Author author = authorService.getAuthorById(vm.getId());
            if (author.getUser().getId() != currentUser.getId()) {
                throw new AccessDeniedException("Just admin or owner can change this author.");
            }
        }
        Author author = setAvatarUrlToUser(authorService.changeAuthorName(vm.getId(), vm.getAuthorName()));

        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAuthor(
            @RequestParam String id
    ) {
        Long idNum = APIUtil.parseStringToLong(id, "id is not a number");
        User currentUser = getCurrentUser();
        if (currentUser.getRole() == RoleName.TRANSLATOR) {
            Author author = authorService.getAuthorById(idNum);
            if (author.getUser() != null && author.getUser().getId() != currentUser.getId()) {
                throw new AccessDeniedException("Just admin or owner can delete this author.");
            }
        }
        authorService.deleteAuthor(idNum);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private User getCurrentUser() {
        log.debug("Get current user from Security Context Holder....");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.getUserByUsername(username);
        return user;
    }

    private String getServerName() {
        String serverName = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        return serverName;
    }

    private List<Author> setAvatarUrlToUser(List<Author> authors) {
        String serverName = getServerName();
        authors.forEach(
                author -> {
                    if (author.getUser() != null && author.getUser().getAvatarUrl() != null) {
                        String avatarUrl = author.getUser().getAvatarUrl();
                        if (!avatarUrl.contains(serverName)) {
                            log.debug("AvatarURL now is: " + avatarUrl);
                            author.getUser().setAvatarUrl(serverName + avatarUrl);
                        }
                    }
                }
        );

        return authors;
    }

    private Author setAvatarUrlToUser(Author author) {
        String serverName = getServerName();
        if (author.getUser() != null && author.getUser().getAvatarUrl() != null) {
            String avatarUrl = author.getUser().getAvatarUrl();
            if (!avatarUrl.contains(serverName)) {
                log.debug("AvatarURL now is: " + avatarUrl);
                author.getUser().setAvatarUrl(serverName + avatarUrl);
            }
        }
        return author;
    }
}
