package com.mangareader.service.impl;

import com.mangareader.domain.Bookmark;
import com.mangareader.domain.Manga;
import com.mangareader.domain.User;
import com.mangareader.repository.BookmarkRepository;
import com.mangareader.service.IBookmarkService;
import com.mangareader.service.IMangaService;
import com.mangareader.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookmarkServiceImpl implements IBookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final IMangaService mangaService;
    private final IUserService userService;

    @Override
    @Transactional
    public void createBookmark(Long mangaId) {
        Manga manga = mangaService.getMangaById(mangaId);
        User user = userService.getCurrentUser();
        if (bookmarkRepository.existsByUserIdAndMangaId(user.getId(), mangaId)) {
            deleteBookmark(user.getId(), mangaId);
        } else {
            Bookmark bookmark = new Bookmark();
            bookmark.setUser(user);
            bookmark.setManga(manga);
            bookmarkRepository.save(bookmark);
        }
    }

    @Override
    public List<Bookmark> getBookmarksOfCurrentUser() {
        User user = userService.getCurrentUser();
        return bookmarkRepository.findByUserIdOrderByIdDesc(user.getId());
    }

    @Override
    @Transactional
    public void deleteBookmark(Long userId, Long mangaId) {
        bookmarkRepository.deleteByUserIdAndMangaId(userId, mangaId);
    }
}
