package com.mangareader.service.impl;

import com.mangareader.domain.Genre;
import com.mangareader.exception.BadRequestException;
import com.mangareader.exception.DataAlreadyExistsException;
import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.repository.GenreRepository;
import com.mangareader.service.IGenreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GenreServiceImpl implements IGenreService {

    @Autowired
    private GenreRepository genreRepository;

    @Override
    public Genre getGenreById(Long id) {
        log.info("Getting genre by id: " + id);
        return genreRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Genre " + id + " does not exist.")
        );
    }

    @Override
    public Genre getGenreByName(String genreName) {
        log.info("Getting genre by name: " + genreName);
        return genreRepository.findByName(genreName).orElseThrow(
                () -> new ResourceNotFoundException("There is no genre " + genreName + " in the database.")
        );
    }

    @Override
    public List<Genre> getGenreByNameContaining(String genreName) {
        log.info("Getting genre by name: " + genreName);
        List<Genre> result = genreRepository.findByNameContaining(genreName);
        return result;
    }

    @Override
    public List<Genre> getAllGenre() {
        log.info("Getting all genre from the database.");
        List<Genre> result = genreRepository.findAll();
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("There are no Genre in the database.");
        }
        return result;
    }

    @Override
    public List<Genre> getAllPaginateGenreSortedByName(int limit, int offset) {
        if (limit <= 0) {
            throw new BadRequestException("limit must be greater than 0.");
        }
        if (offset < 0) {
            throw new BadRequestException("offset must be greater than or equal to 0.");
        }
        List<Genre> result = genreRepository.findLimitGenreAndSortByName(limit, offset);
        return result;
    }

    @Override
    public List<Genre> getAllGenreSortedByName() {
        log.info("Getting all genre from the database.");
        List<Genre> result = genreRepository.findAllByOrderByNameAsc();
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("There are no genre in the database.");
        }
        return result;
    }

    @Override
    public Genre createNewGenre(Genre genre) {

        if (genre.getId() != null) {
            throw new BadRequestException("New genre can not have an id.");
        }
        if (genreRepository.existsByName(genre.getName().toLowerCase())) {
            log.error("Input genreName {} is already exist.", genre.getName());
            throw new DataAlreadyExistsException("Genre is already exists.");
        }

        Genre result;

        try {
            log.debug("Creating new genre.......");
            result = genreRepository.save(genre);
        } catch (Exception ex) {
            throw new BadRequestException("Error when creating genre " + genre.getName());
        }
        return result;
    }

    @Override
    public Genre changeGenreName(Long id, String genreName) {
        if (!genreRepository.existsById(id)) {
            log.error("Genre {} is not exist.", id);
            throw new BadRequestException("Genre " + id + " does not exist yet.");
        }

        if (genreRepository.existsByName(genreName.toLowerCase())) {
            log.error("Genre {} is already exist.", genreName);
            throw new DataAlreadyExistsException("Genre " + genreName + " is already exist.");
        }

        Genre genre = getGenreById(id);
        genre.setName(genreName);

        Genre result = genreRepository.save(genre);
        return result;
    }

    @Override
    public Genre changeGenreName(Genre genre) {

        if (genreRepository.existsByName(genre.getName())) {
            throw new DataAlreadyExistsException("Genre " + genre.getName() + " is already exist.");
        }
        log.debug("Change genre name.");
        Genre result = genreRepository.save(genre);
        return null;
    }

    @Override
    public void deleteGenre(Long id) {
        genreRepository.deleteById(id);
    }
}
