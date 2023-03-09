package com.mangareader.service;

import com.mangareader.domain.Author;
import com.mangareader.domain.User;

import java.util.List;
import java.util.Set;

public interface IAuthorService {

    Author createAuthor(Author author);

    List<Author> getAllAuthor();

    Author getAuthorById(Long id);

    Set<Author> getAuthorByIds(Set<Long> ids);

    List<Author> getAuthorsByName(String name);

    List<Author> getLimitAuthor(int limit, int offset);

    List<Author> getAuthorByCreatedUser(Long userId);

    User getUserByAuthor(Long authorId);

    Author changeAuthorName(Long id, String name);

    void deleteAuthor(Long id);

}
