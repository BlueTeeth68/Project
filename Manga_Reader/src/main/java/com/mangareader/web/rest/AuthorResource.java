package com.mangareader.web.rest;

import com.mangareader.domain.Author;
import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.exception.BadRequestException;
import com.mangareader.service.IAuthorService;
import com.mangareader.service.IUserService;
import com.mangareader.service.util.APIUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/author")
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@Slf4j
public class AuthorResource {

    private final IAuthorService authorService;

    private final IUserService userService;

    @GetMapping("/list")
    public ResponseEntity<List<Author>> getLimitAuthor(
            @RequestParam(required = false) String limit,
            @RequestParam(required = false) String page
    ) {
        List<Author> authors;

        if (limit == null || page == null) {
            authors = authorService.getLimitAuthor(50, 0);
        } else {
            int limitNum = APIUtil.parseStringToInteger(limit, "Limit is not a number exception.");
            int pageNum = APIUtil.parseStringToInteger(page, "Page is not a number exception");
            int offset = limitNum * (pageNum - 1);

            authors = authorService.getLimitAuthor(limitNum, offset);
        }

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

        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    //find Authors by user_id
    @GetMapping("/created-by")
    public ResponseEntity<List<Author>> getAuthorByUserId(
            @RequestParam String userId
    ) {
        Long userIdNum = APIUtil.parseStringToLong(userId, "userId is not a number exception");

        List<Author> result = authorService.getAuthorByCreatedUser(userIdNum);

        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<Author> createNewAuthor(
            @Valid @RequestBody Author author
    ) {
        User createdBy = getCurrentUser();
        author.setUser(createdBy);
        Author result = authorService.createAuthor(author);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PatchMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<Author> changeAuthorName(
            @RequestParam String id,
            @RequestParam String name
    ) {
        Long idNum = APIUtil.parseStringToLong(id, "id is not a number exception");

        User currentUser = getCurrentUser();
        if (currentUser.getRole() == RoleName.TRANSLATOR) {
            Author author = authorService.getAuthorById(idNum);
            if (author.getUser().getId() != currentUser.getId()) {
                throw new AccessDeniedException("You are not allowed to delete this author.");
            }
        }

        Author author = authorService.changeAuthorName(idNum, name);

        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<?> deleteAuthor(
            @RequestParam String id
    ) {
        Long idNum = APIUtil.parseStringToLong(id, "id is not a number");
        User currentUser = getCurrentUser();
        if (currentUser.getRole() == RoleName.TRANSLATOR) {
            Author author = authorService.getAuthorById(idNum);
            if (author.getUser().getId() != currentUser.getId()) {
                throw new AccessDeniedException("You are not allowed to delete this author.");
            }
        }
        authorService.deleteAuthor(idNum);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private User getCurrentUser() {
        log.debug("Get current user from Security Context Holder....");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.getUserByUsername(username);
        return user;
    }


}
