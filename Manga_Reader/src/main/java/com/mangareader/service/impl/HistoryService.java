package com.mangareader.service.impl;

import com.mangareader.domain.*;
import com.mangareader.exception.BadRequestException;
import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.repository.HistoryRepository;
import com.mangareader.service.IChapterService;
import com.mangareader.service.IHistoryService;
import com.mangareader.service.IMangaService;
import com.mangareader.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService implements IHistoryService {

    private final HistoryRepository historyRepository;
    private final IUserService userService;
    private final IChapterService chapterService;
    private final IMangaService mangaService;

    @Override
    public History getHistoryById(HistoryId historyId) {
        return historyRepository.findById(historyId).orElseThrow(
                () -> new ResourceNotFoundException("The history does not exist.")
        );
    }

    @Override
    @Transactional
    public void createNewHistory(Long chapterId) {
        User user = userService.getCurrentUser();
        Chapter chapter = chapterService.getChapterById(chapterId);
        Manga manga = chapter.getManga();
        HistoryId historyId = new HistoryId();
        historyId.setUser(user);
        historyId.setManga(manga);
        if (historyRepository.existsById(historyId)) {
            History history = getHistoryById(historyId);
            history.setChapter(chapter);
            history.setDate(LocalDateTime.now());
            historyRepository.save(history);
        } else {
            History history = new History();
            history.setUser(user);
            history.setManga(manga);
            history.setChapter(chapter);
            history.setDate(LocalDateTime.now());
            historyRepository.save(history);
        }
    }

    @Override
    public Page<History> getAllHistoryOfCurrentUser(Integer page, Integer size) {
        if (page == null || page < 0) {
            throw new BadRequestException("Page must be greater than or equal to 0.");
        }
        if (size == null || size <= 0) {
            throw new BadRequestException("Size must be greater than 0.");
        }
        User user = userService.getCurrentUser();
        Pageable pageOption = PageRequest.of(page, size, Sort.by("date").descending());
        return historyRepository.findByUserId(user.getId(), pageOption);
    }

    @Override
    public void deleteHistory(Long mangaId) {
        User user = userService.getCurrentUser();
        Manga manga = mangaService.getMangaById(mangaId);
        HistoryId id = new HistoryId(user, manga);
        historyRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteListHistory(List<Long> mangaIds) {
        if (mangaIds == null || mangaIds.isEmpty()) {
            throw new BadRequestException("List manga id is null or empty.");
        }
        //optimize later
        for (Long mangaId : mangaIds) {
            deleteHistory(mangaId);
        }
    }
}
