package com.mangareader.service;

import com.mangareader.domain.Genre;

import java.util.List;

public interface IGenreService {

    Genre getGenreById(Long id);

    Genre getGenreByName(String genreName);

    List<Genre> getGenreByNameContaining(String genreName);

    List<Genre> getAllGenre();

    List<Genre> getAllPaginateGenreSortedByName(int limit, int offset);

    List<Genre> getAllGenreSortedByName();

    Genre createNewGenre(Genre genre);

    Genre changeGenreName(Long id, String genreName);

    Genre changeGenreName(Genre genre);

    void deleteGenre(Long id);

}
