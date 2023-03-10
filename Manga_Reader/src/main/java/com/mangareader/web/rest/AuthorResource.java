package com.mangareader.web.rest;

import com.mangareader.domain.Author;
import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
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

import java.util.List;

@RestController
@RequestMapping("/author")
@RequiredArgsConstructor
@Slf4j
public class AuthorResource {
    private final IAuthorService authorService;
    private final IUserService userService;
    private final HttpServletRequest request;

//    private final String SERVER_NAME = APIUtil.getServerName(request);

    @GetMapping("/list")
    public ResponseEntity<List<Author>> getLimitAuthor(
            @RequestParam(defaultValue = "50") String limit,
            @RequestParam(defaultValue = "1") String page
    ) {
        List<Author> authors = authorService.getLimitAuthor(limit, page);
        authors = authorService.setAvatarUrlToUser(authors, APIUtil.getServerName(request));
        return new ResponseEntity<>(authors, HttpStatus.FOUND);
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAuthorByIdOrName(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name
    ) {
        List<Author> result = authorService.getAuthorByIdOrName(id, name);
        result = authorService.setAvatarUrlToUser(result, APIUtil.getServerName(request));
        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @GetMapping("/created-by")
    public ResponseEntity<List<Author>> getAuthorByUserId(
            @RequestParam String userId
    ) {
        List<Author> result = authorService.getAuthorByCreatedUser(userId);
        result = authorService.setAvatarUrlToUser(result, APIUtil.getServerName(request));
        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<Author> createNewAuthor(
            @RequestBody String name
    ) {
        User user = getCurrentUser();
        Author author = authorService.createAuthor(name, user.getId());
        author = authorService.setAvatarUrlToUser(author, APIUtil.getServerName(request));
        return new ResponseEntity<>(author, HttpStatus.CREATED);
    }

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
        Author author = authorService.changeAuthorName(vm.getId(), vm.getAuthorName());
        author = authorService.setAvatarUrlToUser(author, APIUtil.getServerName(request));
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

}
