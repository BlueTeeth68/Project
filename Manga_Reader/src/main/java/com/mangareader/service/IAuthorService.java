package com.mangareader.service;

import com.mangareader.domain.Author;
import com.mangareader.domain.User;

import java.util.List;

public interface IAuthorService {

    Author createAuthor(Author author);

    List<Author> getAllAuthor();

    List<Author> getAuthorByCreatedUser(Long userId);

    User getUserByAuthor(Long authorId);

    Author changeAuthorName(Author author);

    void deleteAuthor(Long id);

}
