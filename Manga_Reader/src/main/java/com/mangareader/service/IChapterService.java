package com.mangareader.service;

import com.mangareader.domain.Chapter;
import com.mangareader.domain.Manga;
import com.mangareader.web.rest.vm.ChangeChapterVM;
import com.mangareader.web.rest.vm.CreateChapterVM;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("unused")
public interface IChapterService {

    Chapter getChapterById(Long chapterId);

    List<Chapter> getChapterByMangaId(Long mangaId);

    Chapter createChapter(CreateChapterVM vm);

    Manga updateChapterInformation(ChangeChapterVM vm);

    Manga deleteChapter(Long chapterId);

    void deleteAllChapterImageOfChapter(Long chapterId);

    Manga addImagesToChapter(MultipartFile[] files, Long chapterId) throws TimeoutException;

}
