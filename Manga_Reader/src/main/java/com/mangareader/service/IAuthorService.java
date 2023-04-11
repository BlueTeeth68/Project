package com.mangareader.service;

import com.mangareader.domain.Author;
import com.mangareader.domain.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public interface IAuthorService {

    Author createAuthor(Author author);

    Author createAuthor(String name);

    List<Author> getAllAuthor();

    Author getAuthorById(Long id);

    Set<Author> getAuthorByIds(Set<Long> ids);

    List<Author> getAuthorsByName(String name);

    Page<Author> getLimitAuthor(int size, int page);

    List<Author> getAuthorByIdOrName(Long id, String name);

    List<Author> getAuthorByCreatedUser(Long userId);

    User getUserByAuthor(Long authorId);

    Long getNumberOfAuthor();

    Author changeAuthorName(Long authorId, String name);

    void deleteAuthor(Long authorId);

    void deleteAuthor(String id);

    void checkAuthorize(Long authorId, Long userId);

}
