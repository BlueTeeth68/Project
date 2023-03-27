package com.mangareader.service;

import com.mangareader.domain.Bookmark;

import java.util.List;

public interface IBookmarkService {

    void createBookmark(Long mangaId);

    List<Bookmark> getBookmarksOfCurrentUser();

    void deleteBookmark(Long userId, Long mangaId);
}
