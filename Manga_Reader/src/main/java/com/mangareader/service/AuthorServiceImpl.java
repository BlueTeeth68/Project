package com.mangareader.service;

import com.mangareader.domain.Author;
import com.mangareader.domain.User;
import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AuthorServiceImpl implements IAuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public Author createAuthor(Author author) {
        Author result = authorRepository.save(author);
        return result;
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
    public List<Author> getAuthorByCreatedUser(Long userId) {

        List<Author> result = authorRepository.findByCreatedBy(userId);

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
    public Author changeAuthorName(Author author) {
        Author result = authorRepository.save(author);
        return result;
    }

    @Override
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }
}
