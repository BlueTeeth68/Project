package com.mangareader.service.impl;

import com.mangareader.domain.Author;
import com.mangareader.domain.User;
import com.mangareader.exception.BadRequestException;
import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.repository.AuthorRepository;
import com.mangareader.service.IAuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthorServiceImpl implements IAuthorService {

    private final AuthorRepository authorRepository;

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
    public List<Author> getAuthorByCreatedUser(Long userId) {

        List<Author> result = authorRepository.findByUser(userId);

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
    public Author changeAuthorName(Long id, String name) {
        if (id == null) {
            throw new BadRequestException("Author id is invalid");
        }
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Name is null or blank.");
        }
        Author tmp = getAuthorById(id);
        tmp.setName(name);
        Author result = authorRepository.save(tmp);
        return result;
    }

    @Override
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }
}
