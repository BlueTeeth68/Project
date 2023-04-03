package com.mangareader.service.impl;

import com.mangareader.domain.Chapter;
import com.mangareader.domain.ChapterImage;
import com.mangareader.domain.Manga;
import com.mangareader.exception.DataAlreadyExistsException;
import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.repository.ChapterImageRepository;
import com.mangareader.repository.ChapterRepository;
import com.mangareader.service.IChapterService;
import com.mangareader.service.IMangaService;
import com.mangareader.service.IStorageService;
import com.mangareader.service.util.APIUtil;
import com.mangareader.web.rest.vm.ChangeChapterVM;
import com.mangareader.web.rest.vm.CreateChapterVM;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChapterServiceImpl implements IChapterService {

    private final String MANGA_FOLDER = "./image/manga/";
    private final ChapterRepository chapterRepository;
    private final IMangaService mangaService;
    private final IStorageService storageService;
    private final ChapterImageRepository chapterImageRepository;

    @Override
    public Chapter getChapterById(Long chapterId) {
        return chapterRepository.findById(chapterId).orElseThrow(
                () -> new ResourceNotFoundException("There are no chapter " + chapterId + " in database.")
        );
    }

    @Override
    public List<Chapter> getChapterByMangaId(Long mangaId) {
        return chapterRepository.findByMangaIdOrderByIdDesc(mangaId);
    }

    @Override
    @Transactional
    public Chapter createChapter(CreateChapterVM vm) {
        mangaService.checkMangaAuthorize(vm.getMangaId());
        if (chapterRepository.existsByMangaIdAndChapterNumber(vm.getMangaId(), vm.getChapterNumber())) {
            throw new DataAlreadyExistsException("This chapter number has been existed.");
        }
        Manga manga = mangaService.getMangaById(vm.getMangaId());
        Chapter chapter = new Chapter();
        chapter.setName(vm.getName());
        chapter.setChapterNumber(vm.getChapterNumber());
        chapter.setManga(manga);
        chapter.setCreatedDate(LocalDateTime.now());
        chapter = chapterRepository.save(chapter);

        //update the latest update of manga
        manga.setLatestUpdate(chapter.getCreatedDate());
        mangaService.saveManga(manga);
        return chapter;
    }

    @Override
    @Transactional
    public Manga updateChapterInformation(ChangeChapterVM vm) {
        Chapter chapter = getChapterById(vm.getId());
        Manga manga = chapter.getManga();
        mangaService.checkMangaAuthorize(manga.getId());
        if (vm.getName() != null && !vm.getName().isBlank()) {
            chapter.setName(vm.getName());
        }
        if (vm.getChapterNumber() != null) {
            if (chapterRepository.existsByMangaIdAndChapterNumber(manga.getId(), vm.getChapterNumber())) {
                throw new DataAlreadyExistsException("This chapter number has been existed.");
            } else {
                chapter.setChapterNumber(vm.getChapterNumber());
            }
        }
        chapterRepository.save(chapter);
        return manga;
    }

    @Override
    @Transactional
    public Manga deleteChapter(Long chapterId) {
        Chapter chapter = getChapterById(chapterId);
        Manga manga = chapter.getManga();
        Long mangaId = manga.getId();
        mangaService.checkMangaAuthorize(mangaId);
        chapterRepository.deleteById(chapterId);
        return mangaService.getMangaById(mangaId);
    }

    @Override
    public Manga deleteChapter(String chapterId) {
        Long id = APIUtil.parseStringToLong(chapterId, "id is not a number.");
        return deleteChapter(id);
    }

    @Override
    @Transactional
    public void deleteAllChapterImageOfChapter(Long chapterId) {
        chapterImageRepository.deleteByChapterId(chapterId);
    }

    @Override
    @Transactional
    public Manga addImagesToChapter(MultipartFile[] files, Long chapterId) {
        log.info("File length: {}", files.length);

        Chapter chapter = getChapterById(chapterId);
        Manga manga = chapter.getManga();
        Float chapterNumber = chapter.getChapterNumber();
        Long mangaId = manga.getId();
        mangaService.checkMangaAuthorize(mangaId);

        String location = "./image/manga/manga" + mangaId + "/" + chapterNumber;
        log.info("Check file length before pass: {}", files.length);
        log.info("Location before pass: {}", location);
        List<String> fileNames = storageService.storeMultipleFile(files, location);
        deleteAllChapterImageOfChapter(chapterId);
        fileNames.forEach(
                name -> {
                    ChapterImage chapterImage = new ChapterImage();
                    chapterImage.setChapter(chapter);
                    chapterImage.setImageUrl("/image/manga/manga" + mangaId + "/" + chapterNumber + "/" + name);
                    chapterImageRepository.save(chapterImage);
                }
        );
        //update the latest update of manga
        manga.setLatestUpdate(LocalDateTime.now());
        manga = mangaService.saveManga(manga);
        return manga;
    }

    @Override
    public Resource getChapterImage(String fileName) {
        return storageService.loadAsResource(fileName, MANGA_FOLDER);
    }
}
