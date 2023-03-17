package com.mangareader.service.impl;

import com.mangareader.domain.*;
import com.mangareader.exception.BadRequestException;
import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.repository.MangaRepository;
import com.mangareader.service.*;
import com.mangareader.service.util.APIUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class MangaServiceImpl implements IMangaService {

    @Autowired
    private MangaRepository mangaRepository;

    @Autowired
    private IGenreService genreService;

    @Autowired
    private IAuthorService authorService;

    @Autowired
    private IStorageService storageService;

    @Autowired
    private IUserService userService;

    private final String MANGA_FOLDER = "./image/manga/";

    @Override
    public Manga getMangaById(Long id) {
        log.info("Getting manga " + id + " in the database.......");
        return mangaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Manga " + id + " does not exist.")
        );
    }

    @Override
    public Manga getMangaById(String id) {
        Long idNum = APIUtil.parseStringToLong(id, "id is not a number.");
        return getMangaById(idNum);
    }

    //    @Override
//    public List<Manga> getMangaByGenre(Long genreId) {
//        log.info("Getting genre by id.......");
//        Genre genre = genreService.getGenreById(genreId);
//        log.info("Getting list of manga from genre........");
//        List<Manga> result = genre.getMangas().stream().toList();
//        if (result.isEmpty()) {
//            log.error("Resource not found");
//            throw new ResourceNotFoundException("There are no manga with genre " + genre.getName());
//        }
//        return result;
//    }

    @Override
    public List<Manga> getMangaByGenre(Long genreID, int limit, int offset) {
        if (limit <= 0) {
            log.error("Invalid limit");
            throw new BadRequestException("limit must be greater than 0.");
        }
        if (offset < 0) {
            log.error("Invalid offset");
            throw new BadRequestException("offset must be greater than or equal to 0.");
        }
        log.info("Getting list of manga by genreId {} with limit {} and offset {}", genreID, limit, offset);
        List<Manga> mangas = mangaRepository.findLimitMangaByGenreID(genreID, limit, offset);
        if (mangas.isEmpty()) {
            log.error("Resource not found");
            throw new ResourceNotFoundException("Resource not found.");
        }
        return mangas;
    }

    @Override
    public List<Manga> getMangaByGenre(String genreId, String limit, String page) {
        Long genreIdNum = APIUtil.parseStringToLong(genreId, "genreId is not a number.");
        int limitNum = APIUtil.parseStringToInteger(limit, "limit is not a number.");
        int pageNum = APIUtil.parseStringToInteger(page, "page is not a number.");
        int offset = limitNum * (pageNum - 1);
        return getMangaByGenre(genreIdNum, limitNum, offset);
    }

    @Override
    public List<Manga> getMangaByAuthor(Long authorId, int limit, int offset) {
        if (limit <= 0) {
            log.error("Invalid limit");
            throw new BadRequestException("limit must be greater than 0.");
        }
        if (offset < 0) {
            log.error("Invalid offset");
            throw new BadRequestException("offset must be greater than or equal to 0.");
        }
        log.info("Getting list of manga by authorId {} with limit {} and offset {}", authorId, limit, offset);
        List<Manga> mangas = mangaRepository.findLimitMangaByAuthorId(authorId, limit, offset);
        if (mangas.isEmpty()) {
            log.error("Resource not found.");
            throw new ResourceNotFoundException("Resource not found.");
        }
        return mangas;
    }

    @Override
    public List<Manga> getMangaByAuthor(String authorId, String limit, String page) {
        Long authorIdNum = APIUtil.parseStringToLong(authorId, "authorId is not a number.");
        int limitNum = APIUtil.parseStringToInteger(limit, "limit is not a number.");
        int pageNum = APIUtil.parseStringToInteger(page, "page is not a number.");
        int offset = limitNum * (pageNum - 1);
        return getMangaByAuthor(authorIdNum, limitNum, offset);
    }

    @Override
    public List<Manga> getMangaByTranslator(Long translatorId, int limit, int offset) {
        if (limit <= 0) {
            log.error("Invalid limit");
            throw new BadRequestException("limit must be greater than 0.");
        }
        if (offset < 0) {
            log.error("Invalid offset");
            throw new BadRequestException("offset must be greater than or equal to 0.");
        }
        log.info("Getting list of manga by translatorId {} with limit {} and offset {}", translatorId, limit, offset);
        List<Manga> mangas = mangaRepository.findByUserIdOrderByName(translatorId, limit, offset);
        if (mangas.isEmpty()) {
            log.error("Resource not found.");
            throw new ResourceNotFoundException("Resource not found.");
        }
        return mangas;
    }

    @Override
    public List<Manga> getMangaByTranslator(String translatorId, String limit, String page) {
        Long translatorIdNum = APIUtil.parseStringToLong(translatorId, "translatorId is not a number.");
        int limitNum = APIUtil.parseStringToInteger(limit, "limit is not a number.");
        int pageNum = APIUtil.parseStringToInteger(page, "page is not a number.");
        int offset = limitNum * (pageNum - 1);
        return getMangaByTranslator(translatorIdNum, limitNum, offset);
    }

    @Override
    public List<Manga> getMangaByNameOrKeyword(String keyword, int limit, int offset) {

        if (keyword == null || keyword.isBlank()) {
            log.error("Invalid keyword.");
            throw new BadRequestException("Keyword is null or blank.");
        }
        if (limit <= 0) {
            log.error("Invalid limit.");
            throw new BadRequestException("limit must be greater than 0.");
        }
        if (offset < 0) {
            log.error("Invalid offset.");
            throw new BadRequestException("offset must be greater than or equal to 0.");
        }
        log.info("Getting list of manga by keyword {} with limit {} and offset {}", keyword, limit, offset);
        List<Manga> mangas = mangaRepository.findByNameOrKeywordOrderByName(keyword, limit, offset);
        if (mangas.isEmpty()) {
            log.error("Resource not found.");
            throw new ResourceNotFoundException("Resource not found.");
        }
        return mangas;
    }

    @Override
    public List<Manga> getMangaByNameOrKeyword(String keyword, String limit, String page) {
        int limitNum = APIUtil.parseStringToInteger(limit, "limit is not a number.");
        int pageNum = APIUtil.parseStringToInteger(page, "page is not a number.");
        int offset = limitNum * (pageNum - 1);
        return getMangaByNameOrKeyword(keyword, limitNum, offset);
    }

    @Override
    public List<Manga> getSuggestMangas(int limit, int offset) {
        if (limit <= 0) {
            log.error("Invalid limit");
            throw new BadRequestException("limit must be greater than 0.");
        }
        if (offset < 0) {
            log.error("Invalid offset");
            throw new BadRequestException("offset must be greater than or equal to 0.");
        }
        log.info("Getting list of suggest manga with limit {} and offset {}", limit, offset);
        List<Manga> mangas = mangaRepository.findSuggestManga(limit, offset);
        if (mangas.isEmpty()) {
            log.error("Resource not found.");
            throw new ResourceNotFoundException("Resource not found.");
        }
        return mangas;
    }

    @Override
    public List<Manga> getSuggestMangas(String limit, String page) {
        int limitNum = APIUtil.parseStringToInteger(limit, "limit is not a number.");
        int pageNum = APIUtil.parseStringToInteger(page, "page is not a number.");
        int offset = limitNum * (pageNum - 1);
        return getSuggestMangas(limitNum, offset);
    }

    @Override
    public List<Manga> getMangasByStatusLimit(String status, int limit, int offset) {
        if (limit <= 0) {
            log.error("Invalid limit");
            throw new BadRequestException("limit must be greater than 0.");
        }
        if (offset < 0) {
            log.error("Invalid offset");
            throw new BadRequestException("offset must be greater than or equal to 0.");
        }
        log.info("Getting list of manga by status {} with limit {} and offset {}", status, limit, offset);
        List<Manga> mangas = mangaRepository.findMangaByStatusLimit(status, limit, offset);
        if (mangas.isEmpty()) {
            log.error("Resource not found.");
            throw new ResourceNotFoundException("Resource not found.");
        }
        return mangas;
    }

    @Override
    public List<Manga> getMangasByStatusLimit(String status, String limit, String page) {
        MangaStatus mangaStatus = APIUtil.parseStringToMangaStatus(status, "Status must be Ongoing or Completed");
        int limitNum = APIUtil.parseStringToInteger(limit, "limit is not a number.");
        int pageNum = APIUtil.parseStringToInteger(page, "page is not a number.");
        int offset = limitNum * (pageNum - 1);
        return getMangasByStatusLimit(mangaStatus.toString(), limitNum, offset);
    }

    @Override
    public List<Manga> getMangaByName(String name) {
        log.info("Getting mangas from database by name LIKE.......");
        List<Manga> mangas = mangaRepository.findByNameContainingOrderByName(name);
        return mangas;
    }

    @Override
    public List<Manga> getAllManga() {
        log.info("Get all manga from database sorted by id.");
        List<Manga> result = mangaRepository.findAll();
        if (result.isEmpty()) {
            log.error("Resource not found.");
            throw new ResourceNotFoundException("There are no manga in the database.");
        }
        return result;
    }

    @Override
    public List<Manga> getAllMangaSortByLatestUpdate() {
        log.info("Get all manga from database sorted by latest update desc.");
        List<Manga> result = mangaRepository.findAllByOrderByLatestUpdateDesc();

        if (result.isEmpty()) {
            log.error("Resource not found.");
            throw new ResourceNotFoundException("There are no manga in the database.");
        }

        return result;
    }

    @Override
    public List<Manga> getAllPaginateMangaOrderByLatestUpdate(int limit, int offset) {

        if (limit <= 0) {
            log.error("Invalid limit");
            throw new BadRequestException("limit must be greater than 0.");
        }
        if (offset < 0) {
            log.error("Invalid offset");
            throw new BadRequestException("offset must be greater than or equal to 0.");
        }
        log.info("Getting manga sort by latest update with limit {} and offset {}", limit, offset);
        List<Manga> result = mangaRepository.findAllAndPaginateOrderByLatestUpdate(limit, offset);

        return result;
    }

    @Override
    public List<Manga> getAllPaginateMangaOrderByLatestUpdate(String limit, String page) {
        int limitNum = APIUtil.parseStringToInteger(limit, "Limit is not a number exception.");
        int pageNum = APIUtil.parseStringToInteger(page, "Page is not a number exception");
        int offset = limitNum * (pageNum - 1);
        return getAllPaginateMangaOrderByLatestUpdate(limitNum, offset);
    }

    @Override
    public Manga createManga(Manga manga) {
        return mangaRepository.save(manga);
    }

    @Override
    @Transactional
    public Manga addGenreToManga(Long mangaId, Set<String> genreName, String serverName) {
        checkMangaAuthorize(mangaId);
        Manga manga = getMangaById(mangaId);
        Set<Genre> genres = genreService.getGenreByName(genreName);
        manga.setGenres(genres);
        manga = mangaRepository.save(manga);
        if (manga.getCoverImageUrl() != null) {
            manga.setCoverImageUrl(serverName + manga.getCoverImageUrl());
        }
        return manga;
    }

    @Override
    @Transactional
    public Manga addAuthorsToManga(Long mangaId, Set<Long> authorIds, String serverName) {
        checkMangaAuthorize(mangaId);
        Manga manga = getMangaById(mangaId);
        Set<Author> authors = authorService.getAuthorByIds(authorIds);
        manga.setAuthors(authors);
        manga = mangaRepository.save(manga);
        if (manga.getCoverImageUrl() != null) {
            manga.setCoverImageUrl(serverName + manga.getCoverImageUrl());
        }
        return manga;
    }

    @Override
    @Transactional
    public Manga updateCoverImage(Long id, MultipartFile file, String serverName) {

        checkMangaAuthorize(id);
        Manga manga = getMangaById(id);
        String folderName = "manga" + id;
        storageService.store(file, MANGA_FOLDER + folderName, "cover_image");
        String coverImageUrl = /*SERVER_NAME + */ "/image/manga/" + folderName + "/" + "cover_image";
        manga.setCoverImageUrl(coverImageUrl);
        manga = mangaRepository.save(manga);
        if (manga.getCoverImageUrl() != null) {
            manga.setCoverImageUrl(serverName + manga.getCoverImageUrl());
        }

        return manga;
    }

    @Override
    @Transactional
    public Manga updateCoverImage(String id, MultipartFile file, String serverName) {
        Long idNum = APIUtil.parseStringToLong(id, "id is not a number");
        return updateCoverImage(idNum, file, serverName);
    }

    @Override
    @Transactional
    public void deleteManga(Long id) {

    }

    @Override
    public Resource getCoverImage(String fileName) {
        Resource file = storageService.loadAsResource(fileName, MANGA_FOLDER);
        return file;
    }

    @Override
    public Boolean existsById(Long id) {
        return mangaRepository.existsById(id);
    }

    @Override
    public void checkMangaAuthorize(Long mangaId) {
        User user = userService.getCurrentUser();
        if (user.getRole() == RoleName.TRANSLATOR) {
            Manga manga = getMangaById(mangaId);
            if (manga.getUser() == null || (manga.getUser() != null && manga.getUser().getId() != user.getId())) {
                log.error("Access denied.");
                throw new AccessDeniedException("Only owner can update this manga.");
            }
        }

    }
}
