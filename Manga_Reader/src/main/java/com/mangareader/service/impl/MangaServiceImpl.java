package com.mangareader.service.impl;

import com.mangareader.domain.*;
import com.mangareader.exception.BadRequestException;
import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.repository.MangaRepository;
import com.mangareader.service.*;
import com.mangareader.service.util.APIUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class MangaServiceImpl implements IMangaService {

    private final String MANGA_FOLDER = "./image/manga/";
    private final MangaRepository mangaRepository;
    private final IGenreService genreService;
    private final IAuthorService authorService;
    private final IStorageService storageService;
    private final IUserService userService;

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

    @Override
    public Page<Manga> getPageableMangaByGenre(Long genreId, int page, int size) {
        if (page < 0) {
            log.error("Invalid page");
            throw new BadRequestException("page must be greater than 0.");
        }
        if (size <= 0) {
            log.error("Invalid size");
            throw new BadRequestException("size must be greater than 0.");
        }
        Pageable pageOption = PageRequest.of(page, size, Sort.by("name").ascending());
        return mangaRepository.findPageableMangaByGenreId(genreId, pageOption);
    }

    @Override
    public Page<Manga> getPageableMangaByGenre(String genreId, String page, String size) {
        Long genreIdNum = APIUtil.parseStringToLong(genreId, "genreId is not a number.");
        int pageNum = APIUtil.parseStringToInteger(page, "Page is not a number.");
        int sizeNum = APIUtil.parseStringToInteger(size, "Size is not a number.");
        return getPageableMangaByGenre(genreIdNum, pageNum, sizeNum);
    }

    @Override
    public Page<Manga> getPageableMangaByAuthor(Long authorId, int page, int size) {
        if (page < 0) {
            log.error("Invalid page");
            throw new BadRequestException("Page must be greater than 0.");
        }
        if (size <= 0) {
            log.error("Invalid size");
            throw new BadRequestException("Size must be greater than 0.");
        }
        Pageable pageOption = PageRequest.of(page, size, Sort.by("name").ascending());
        return mangaRepository.findPageableMangaByAuthorId(authorId, pageOption);
    }

    @Override
    public Page<Manga> getPageableMangaByAuthor(String authorId, String page, String size) {
        Long authorIdNum = APIUtil.parseStringToLong(authorId, "authorId is not a number.");
        int pageNum = APIUtil.parseStringToInteger(page, "Page is not a number.");
        int sizeNum = APIUtil.parseStringToInteger(size, "Size is not a number.");
        return getPageableMangaByAuthor(authorIdNum, pageNum, sizeNum);
    }

    @Override
    public Page<Manga> getPageableMangaByTranslator(Long translatorId, int page, int size) {
        if (page < 0) {
            log.error("Invalid page");
            throw new BadRequestException("Page must be greater than 0.");
        }
        if (size <= 0) {
            log.error("Invalid size");
            throw new BadRequestException("Size must be greater than 0.");
        }
        Pageable pageOption = PageRequest.of(page, size, Sort.by("name").ascending());
        log.info("Getting list of manga by translatorId {} with page {} and size {}", translatorId, page, size);
        return mangaRepository.findByUserId(translatorId, pageOption);
    }

    @Override
    public Page<Manga> getPageableMangaByTranslator(String translatorId, String page, String size) {
        Long translatorIdNum = APIUtil.parseStringToLong(translatorId, "translatorId is not a number.");
        int pageNum = APIUtil.parseStringToInteger(page, "Page is not a number.");
        int sizeNum = APIUtil.parseStringToInteger(size, "Size is not a number.");
        return getPageableMangaByTranslator(translatorIdNum, pageNum, sizeNum);
    }

    @Override
    public Page<Manga> getPageableMangaByNameOrKeyword(String keyword, int page, int size) {
        if (keyword == null || keyword.isBlank()) {
            log.error("Invalid keyword.");
            throw new BadRequestException("Keyword is null or blank.");
        }
        if (page < 0) {
            log.error("Invalid page.");
            throw new BadRequestException("Page must be greater than 0.");
        }
        if (size <= 0) {
            log.error("Invalid size.");
            throw new BadRequestException("Size must be greater than or equal to 0.");
        }
        Pageable pageOption = PageRequest.of(page, size, Sort.by("name").ascending());
        log.info("Getting list of manga by keyword {} with page {} and size {}", keyword, page, size);
        return mangaRepository.findPageableMangaByNameOrKeyword(keyword, pageOption);
    }

    @Override
    public Page<Manga> getPageableMangaByNameOrKeyword(String keyword, String page, String size) {
        int pageNum = APIUtil.parseStringToInteger(page, "Page is not a number.");
        int sizeNum = APIUtil.parseStringToInteger(size, "Size is not a number.");
        return getPageableMangaByNameOrKeyword(keyword, pageNum, sizeNum);
    }

    @Override
    public Page<Manga> getPageableSuggestManga(int page, int size) {
        if (page < 0) {
            log.error("Invalid page");
            throw new BadRequestException("Page must be greater than 0.");
        }
        if (size <= 0) {
            log.error("Invalid size");
            throw new BadRequestException("Size must be greater than 0.");
        }
        Pageable pageOption = PageRequest.of(page, size);
        log.info("Getting list of suggest manga with page {} and size {}", page, size);
        return mangaRepository.findPageableSuggestManga(pageOption);
    }

    @Override
    public Page<Manga> getPageableSuggestManga(String page, String size) {
        int pageNum = APIUtil.parseStringToInteger(page, "Page is not a number.");
        int sizeNum = APIUtil.parseStringToInteger(size, "Size is not a number.");
        return getPageableSuggestManga(pageNum, sizeNum);
    }

    @Override
    public Page<Manga> getPageableMangaByStatus(String status, int page, int size) {
        if (page < 0) {
            log.error("Invalid page");
            throw new BadRequestException("Page must be greater than 0.");
        }
        if (size <= 0) {
            log.error("Invalid size");
            throw new BadRequestException("Size must be greater than 0.");
        }
        Pageable pageOption = PageRequest.of(page, size, Sort.by("name").ascending());
        log.info("Getting list of manga by status {} with page {} and size {}", status, page, size);
        return mangaRepository.findByStatus(status, pageOption);
    }

    @Override
    public Page<Manga> getPageableMangaByStatus(String status, String page, String size) {
        MangaStatus mangaStatus = APIUtil.parseStringToMangaStatus(status, "Status must be Ongoing or Completed");
        int pageNum = APIUtil.parseStringToInteger(page, "Page is not a number.");
        int sizeNum = APIUtil.parseStringToInteger(size, "Size is not a number.");
        return getPageableMangaByStatus(mangaStatus.toString(), pageNum, sizeNum);
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
    public Page<Manga> getAllPageableMangaOrderByLatestUpdate(int page, int size) {
        if (page < 0) {
            log.error("Invalid page");
            throw new BadRequestException("page must be greater than 0.");
        }
        if (size <= 0) {
            log.error("Invalid size");
            throw new BadRequestException("size must be greater than 0.");
        }
        Pageable pageOption = PageRequest.of(page, size);
        return mangaRepository.findAllByOrderByLatestUpdateDesc(pageOption);
    }

    @Override
    public Page<Manga> getAllPageableMangaOrderByLatestUpdate(String page, String size) {
        int pageNum = APIUtil.parseStringToInteger(page, "Page is not a number exception");
        int sizeNum = APIUtil.parseStringToInteger(size, "Size is not a number exception.");
        return getAllPageableMangaOrderByLatestUpdate(pageNum, sizeNum);
    }

    @Override
    public Manga createManga(Manga manga) {
        if (manga.getId() != null) {
            log.error("manga id is not null");
            throw new BadRequestException("New manga cannot have an ID");
        }
        if (manga.getName() == null || manga.getName().isBlank()) {
            log.error("Manga name is null");
            throw new BadRequestException("Manga name can not be null");
        }
        if (manga.getYearOfPublication() == null) {
            log.error("Year of publication is null");
            throw new BadRequestException("Year of public cation cannot be empty.");
        }
        if (manga.getUser() == null) {
            log.error("Created user of manga is null");
            throw new BadRequestException("User can not be null");
        }
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
        return storageService.loadAsResource(fileName, MANGA_FOLDER);
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
            if (manga.getUser() == null || !manga.getUser().getId().equals(user.getId())) {
                log.error("Access denied.");
                throw new AccessDeniedException("Only owner can update this manga.");
            }
        }

    }
}
