package com.mangareader.service;

import com.mangareader.domain.Author;
import com.mangareader.domain.User;

import java.util.List;
import java.util.Set;

public interface IAuthorService {

    Author createAuthor(Author author);

    Author createAuthor(String name, Long userId);

    List<Author> getAllAuthor();

    Author getAuthorById(Long id);

    Set<Author> getAuthorByIds(Set<Long> ids);

    List<Author> getAuthorsByName(String name);

    List<Author> getLimitAuthor(int limit, int offset);

    List<Author> getLimitAuthor(String limit, String page);

    List<Author> getAuthorByIdOrName(String id, String name);

    List<Author> getAuthorByCreatedUser(Long userId);

    List<Author> getAuthorByCreatedUser(String userId);

    User getUserByAuthor(Long authorId);

    Long getNumberOfAuthor();

    Author changeAuthorName(Long id, String name);

    void deleteAuthor(Long id);

    void deleteAuthor(String id);

    List<Author> setAvatarUrlToUser(List<Author> authors, String serverName);

    Author setAvatarUrlToUser(Author author, String serverName);

}
