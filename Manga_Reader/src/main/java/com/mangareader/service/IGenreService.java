package com.mangareader.service;

import com.mangareader.domain.Genre;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public interface IGenreService {

    Genre getGenreById(Long id);

    Genre getGenreById(String id);

    Genre getGenreByName(String genreName);

    Set<Genre> getGenreByName(Set<String> genreNames);

    List<Genre> getGenreByNameContaining(String genreName);

    List<Genre> getAllGenre();

    Page<Genre> getAllGenreByPagingAndSortByName(int page, int size);
    Page<Genre> getAllGenreByPagingAndSortByName(String page, String size);

    List<Genre> getAllGenreSortedByName();

    Genre createNewGenre(String genreName);

    Genre changeGenreName(Long id, String genreName);

    Genre changeGenreName(Genre genre);

    void deleteGenre(Long id);

    void deleteGenre(String id);

}
