package com.mangareader.service.impl;

import com.mangareader.domain.*;
import com.mangareader.exception.BadRequestException;
import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.repository.MangaRepository;
import com.mangareader.service.*;
import com.mangareader.service.util.APIUtil;
import com.mangareader.web.rest.vm.ChangeMangaVM;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@SuppressWarnings("Unused")
public class MangaServiceImpl implements IMangaService {

    private final String MANGA_COVER_IMAGE_FOLDER = "image/manga/cover-image/";
    private final String MANGA_FOLDER = "image/manga/";

    private final MangaRepository mangaRepository;
    private final IGenreService genreService;
    private final IAuthorService authorService;


    private final IStorageService storageService;

    private final IUserService userService;

    //    @Qualifier("AWSStorageService")
//    @Qualifier("fileSystemStorageService")
    public MangaServiceImpl(MangaRepository mangaRepository, IGenreService genreService, IAuthorService authorService, @Qualifier("fileSystemStorageService") IStorageService storageService, IUserService userService) {
        this.mangaRepository = mangaRepository;
        this.genreService = genreService;
        this.authorService = authorService;
        this.storageService = storageService;
        this.userService = userService;
    }

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
            throw new BadRequestException("Page must be greater than or equal to 0.");
        }
        if (size <= 0) {
            log.error("Invalid size");
            throw new BadRequestException("Size must be greater than 0.");
        }
        Pageable pageOption = PageRequest.of(page, size, Sort.by("name").ascending());
        return mangaRepository.findPageableMangaByGenreId(genreId, pageOption);
    }

    @Override
    public Page<Manga> getPageableMangaByAuthor(Long authorId, int page, int size) {
        if (page < 0) {
            log.error("Invalid page");
            throw new BadRequestException("Page must be greater than or equal to 0.");
        }
        if (size <= 0) {
            log.error("Invalid size");
            throw new BadRequestException("Size must be greater than 0.");
        }
        Pageable pageOption = PageRequest.of(page, size, Sort.by("name").ascending());
        return mangaRepository.findPageableMangaByAuthorId(authorId, pageOption);
    }

    @Override
    public Page<Manga> getPageableMangaByTranslator(Long translatorId, int page, int size) {
        if (page < 0) {
            log.error("Invalid page");
            throw new BadRequestException("Page must be greater than or equal to 0.");
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
    public Page<Manga> getPageableMangaByNameOrKeyword(String keyword, int page, int size) {
        if (keyword == null || keyword.isBlank()) {
            log.error("Invalid keyword.");
            throw new BadRequestException("Keyword is null or blank.");
        }
        if (page < 0) {
            log.error("Invalid page.");
            throw new BadRequestException("Page must be greater than or equal to 0.");
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
    public Page<Manga> getPageableSuggestManga(int page, int size) {
        if (page < 0) {
            log.error("Invalid page");
            throw new BadRequestException("Page must be greater than or equal to 0.");
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
    public Page<Manga> getPageableMangaByStatus(MangaStatus status, int page, int size) {
        if (page < 0) {
            log.error("Invalid page");
            throw new BadRequestException("Page must be greater than or equal to 0.");
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
    public Page<Manga> getAllPageableMangaOrderByLatestUpdate(int page, int size) {
        if (page < 0) {
            log.error("Invalid page");
            throw new BadRequestException("Page must be greater than or equal to 0.");
        }
        if (size <= 0) {
            log.error("Invalid size");
            throw new BadRequestException("size must be greater than 0.");
        }
        Pageable pageOption = PageRequest.of(page, size);
        return mangaRepository.findAllByOrderByLatestUpdateDesc(pageOption);
    }

    @Override
    @Transactional
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
    public Manga addGenreToManga(Long mangaId, Set<String> genreName) {
        checkMangaAuthorize(mangaId);
        Manga manga = getMangaById(mangaId);
        Set<Genre> genres = genreService.getGenreByName(genreName);
        manga.setGenres(genres);
        return mangaRepository.save(manga);
    }

    @Override

    public Manga addAuthorsToManga(Long mangaId, Set<Long> authorIds) {
        checkMangaAuthorize(mangaId);
        Manga manga = getMangaById(mangaId);
        Set<Author> authors = authorService.getAuthorByIds(authorIds);
        manga.setAuthors(authors);
        return mangaRepository.save(manga);
    }

    @Override
    @Transactional
    public Manga updateCoverImage(Long id, MultipartFile file) throws TimeoutException {

        checkMangaAuthorize(id);
        Manga manga = getMangaById(id);
        //get old manga cover image
        String oldUrl = manga.getCoverImageUrl();
        log.info("Old Url is: {}", oldUrl);

        String coverImageUrl = storageService.uploadImage(file, MANGA_COVER_IMAGE_FOLDER, id.intValue());
        log.info("New url is: {}", coverImageUrl);

        manga.setCoverImageUrl(coverImageUrl);
        //delete old cover image
        if (oldUrl != null && !oldUrl.equals(coverImageUrl)) {
            String[] tmp = oldUrl.split("image");
            StringBuffer fileName = new StringBuffer();
            for (int i = 1; i < tmp.length; i++) {
                fileName.append("image");
                fileName.append(tmp[i]);
            }
            log.info("File name is: {}", fileName);
            storageService.deleteFile(fileName.toString());
        }

        return mangaRepository.save(manga);
    }

    @Override
    @Transactional
    public void deleteManga(Long id) {
        checkMangaAuthorize(id);
        mangaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
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

    @Override
    public Manga changeMangaInformation(ChangeMangaVM vm) {

        checkMangaAuthorize(vm.getId());
        Manga manga = getMangaById(vm.getId());
        if (vm.getSummary() != null && !vm.getSummary().isBlank()) {
            manga.setSummary(vm.getSummary());
        }
        if (vm.getName() != null && !vm.getName().isBlank()) {
            manga.setName(vm.getName());
        }
        if (vm.getStatus() != null) {
            manga.setStatus(vm.getStatus());
        }
        if (vm.getYearOfPublication() != null) {
            manga.setYearOfPublication(vm.getYearOfPublication());
        }
        return mangaRepository.save(manga);
    }

    @Override
    public Manga saveManga(Manga manga) {
        return mangaRepository.save(manga);
    }

    @Override
    @Transactional
    public void increaseMangaView(Long mangaId) {
        Manga manga = getMangaById(mangaId);
        manga.setView(manga.getView() + 1);
        mangaRepository.save(manga);
    }

    @Override
    public Resource getCoverImage(String fileName) {
        return storageService.loadAsResource(fileName, MANGA_FOLDER);
    }
}
