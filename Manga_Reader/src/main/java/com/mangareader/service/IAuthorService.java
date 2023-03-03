package com.mangareader.service;

import com.mangareader.domain.Author;
import com.mangareader.domain.User;

import java.util.List;

public interface IAuthorService {

    Author createAuthor(Author author);

    List<Author> getAllAuthor();

    Author getAuthorById(Long id);

    List<Author> getAuthorsByName(String name);

    List<Author> getLimitAuthor(int limit, int offset);

    List<Author> getAuthorByCreatedUser(Long userId);

    User getUserByAuthor(Long authorId);

    Author changeAuthorName(Long id, String name);

    void deleteAuthor(Long id);

}
