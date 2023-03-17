package com.mangareader.service;

import com.mangareader.domain.Genre;

import java.util.List;
import java.util.Set;

public interface IGenreService {

    Genre getGenreById(Long id);

    Genre getGenreById(String id);

    Genre getGenreByName(String genreName);

    Set<Genre> getGenreByName(Set<String> genreNames);

    List<Genre> getGenreByNameContaining(String genreName);

    List<Genre> getAllGenre();

    List<Genre> getAllPaginateGenreSortedByName(int limit, int offset);

    List<Genre> getAllPaginateGenreSortedByName(String limit, String offset);

    List<Genre> getAllGenreSortedByName();

    Genre createNewGenre(Genre genre);

    Genre changeGenreName(Long id, String genreName);

    Genre changeGenreName(Genre genre);

    void deleteGenre(Long id);

    void deleteGenre(String id);

}
