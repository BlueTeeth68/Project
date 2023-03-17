package com.mangareader.service.impl;

import com.mangareader.domain.Author;
import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.exception.BadRequestException;
import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.repository.AuthorRepository;
import com.mangareader.service.IAuthorService;
import com.mangareader.service.IUserService;
import com.mangareader.service.util.APIUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthorServiceImpl implements IAuthorService {

    private final AuthorRepository authorRepository;
    private final IUserService userService;

    @Override
    public Author createAuthor(Author author) {

        if (author.getId() != null) {
            log.error("Author already has id exception.");
            throw new BadRequestException("Author can not already have an id");
        }

        Author result = authorRepository.save(author);
        return result;
    }

    @Override
    public Author createAuthor(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("name is null or blank");
        }
        Author author = new Author();
        author.setName(name);
        User createdBy = userService.getCurrentUser();
        author.setUser(createdBy);
        return createAuthor(author);
    }

    @Override
    public List<Author> getAllAuthor() {
        List<Author> result = authorRepository.findAll();
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("There are no author in the database.");
        }
        return result;
    }

    @Override
    public Author getAuthorById(Long id) {
        Author result = authorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("There are no author " + id + " in the database")
        );
        return result;
    }

    @Override
    public Set<Author> getAuthorByIds(Set<Long> ids) {
        Set<Author> result = new HashSet<>();
        ids.forEach(id -> {
            Author tmp = getAuthorById(id);
            result.add(tmp);
        });
        return result;
    }

    @Override
    public List<Author> getAuthorsByName(String name) {
        List<Author> authors = authorRepository.findByNameContaining(name);
        return authors;
    }

    @Override
    public List<Author> getLimitAuthor(int limit, int offset) {
        if (limit <= 0) {
            throw new BadRequestException("limit must be greater than 0.");
        }
        if (offset < 0) {
            throw new BadRequestException("offset must be greater than or equal to 0.");
        }
        List<Author> result = authorRepository.findLimitAuthor(limit, offset);
        return result;
    }

    @Override
    public List<Author> getLimitAuthor(String limit, String page) {
        int limitNum = APIUtil.parseStringToInteger(limit, "Limit is not a number exception.");
        int pageNum = APIUtil.parseStringToInteger(page, "Page is not a number exception");
        int offset = limitNum * (pageNum - 1);
        return getLimitAuthor(limitNum, offset);
    }

    @Override
    public List<Author> getAuthorByIdOrName(String id, String name) {
        List<Author> result = new ArrayList<>();

        //find by id
        if (id != null && name == null) {
            Long idNum = APIUtil.parseStringToLong(id, "id is not a number exception.");
            result.add(getAuthorById(idNum));
        }
        //find by name
        else if (id == null && name != null) {
            result = getAuthorsByName(name);
        } else {
            throw new BadRequestException("Bad request for id and name value.");
        }

        return result;
    }

    @Override
    public List<Author> getAuthorByCreatedUser(Long userId) {
        List<Author> result = authorRepository.findByUser(userId);
        return result;
    }

    @Override
    public List<Author> getAuthorByCreatedUser(String userId) {
        Long userIdNum = APIUtil.parseStringToLong(userId, "userId is not a number exception");
        List<Author> result = getAuthorByCreatedUser(userIdNum);
        return result;
    }

    @Override
    public User getUserByAuthor(Long authorId) {
        Author author = authorRepository.findById(authorId).orElseThrow(
                () -> {
                    throw new ResourceNotFoundException("There are no author " + authorId + "in the database");
                }
        );
        User result = author.getUser();
        return result;
    }

    @Override
    public Long getNumberOfAuthor() {
        Long result = authorRepository.count();
        return result;
    }

    @Override
    @Transactional
    public Author changeAuthorName(Long authorId, String name) {
        if (authorId == null) {
            throw new BadRequestException("Author id is invalid");
        }
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Name is null or blank.");
        }
        User user = userService.getCurrentUser();
        checkAuthorize(authorId, user.getId());
        Author tmp = getAuthorById(authorId);
        tmp.setName(name);
        Author result = authorRepository.save(tmp);
        return result;
    }

    @Override
    @Transactional
    public void deleteAuthor(Long authorId) {
        authorRepository.deleteById(authorId);
    }

    @Override
    @Transactional
    public void deleteAuthor(String authorId) {
        User user = userService.getCurrentUser();
        Long idNum = APIUtil.parseStringToLong(authorId, "id is not a number");
        checkAuthorize(idNum, user.getId());
        deleteAuthor(idNum);
    }

    @Override
    public List<Author> setAvatarUrlToUser(List<Author> authors, String serverName) {
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

    @Override

    public Author setAvatarUrlToUser(Author author, String serverName) {
        if (author.getUser() != null && author.getUser().getAvatarUrl() != null) {
            String avatarUrl = author.getUser().getAvatarUrl();
            if (!avatarUrl.contains(serverName)) {
                log.debug("AvatarURL now is: " + avatarUrl);
                author.getUser().setAvatarUrl(serverName + avatarUrl);
            }
        }
        return author;
    }

    @Override
    public void checkAuthorize(Long authorId, Long userId) {
        User user = userService.getUserById(userId);
        if (user.getRole() == RoleName.TRANSLATOR) {
            Author author = getAuthorById(authorId);
            if (author.getUser() == null || (author.getUser() != null && author.getUser().getId() != user.getId())) {
                throw new AccessDeniedException("Just admin or owner can delete this author.");
            }
        }
    }
}
