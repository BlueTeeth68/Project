package com.mangareader.service.impl;

import com.mangareader.domain.Keyword;
import com.mangareader.domain.KeywordId;
import com.mangareader.domain.Manga;
import com.mangareader.exception.BadRequestException;
import com.mangareader.exception.DataAlreadyExistsException;
import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.repository.KeywordRepository;
import com.mangareader.service.IKeywordService;
import com.mangareader.service.IMangaService;
import com.mangareader.service.util.APIUtil;
import com.mangareader.web.rest.vm.ChangeKeywordVM;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class KeywordServiceImpl implements IKeywordService {

    private final KeywordRepository keywordRepository;

    private final IMangaService mangaService;

    @Override
    @Transactional
    public Keyword createKeyWord(Keyword keyword) {

        if (keyword.getName() == null || keyword.getName().isBlank()) {
            log.error("Invalid keyword.");
            throw new BadRequestException("Keyword's name is null or blank.");
        }
        if (keyword.getManga() == null || !mangaService.existsById(keyword.getManga().getId())) {
            log.error("Invalid manga.");
            throw new BadRequestException("Manga does not exist.");
        }
        if (keywordRepository.existsById(new KeywordId(keyword.getName(), keyword.getManga()))) {
            log.error("Data already exists.");
            throw new BadRequestException("This key word is already exists.");
        }

        log.info("Saving keyword to database....");
        return keywordRepository.save(keyword);
    }

    @Override
    public Keyword getKeywordByKeywordId(String name, Long mangaId) {

        if (name == null || name.isBlank()) {
            throw new BadRequestException("Invalid keyword name: null or blank.");
        }
        Manga manga = mangaService.getMangaById(mangaId);

        KeywordId id = new KeywordId(name, manga);
        log.info("Finding keyword by KeywordId");
        return keywordRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Keyword name " + id.getName() + " in manga "
                        + id.getManga().getId() + " does not exist.")
        );
    }

    @Override
    public Keyword getKeywordByKeywordId(String name, String mangaId) {
        Long mangaIdNum = APIUtil.parseStringToLong(mangaId, "mangaId is not a number exception.");
        return getKeywordByKeywordId(name, mangaIdNum);
    }

    //need to testing here
    @Override
    public List<Keyword> getKeywordsOfMangaSortedByName(Long mangaId) {

        log.info("Finding keyword by mangaId........");
        return keywordRepository.findByMangaIdOrderByName(mangaId);
    }

    @Override
    public List<Keyword> getKeywordsOfMangaSortedByName(String mangaId) {
        Long mangaIdNum = APIUtil.parseStringToLong(mangaId, "MangaId is not a number.");
        return getKeywordsOfMangaSortedByName(mangaIdNum);
    }

    @Override
    @Transactional
    public Manga addKeywordToManga(Long mangaId, List<String> keywords, String serverName) {
        mangaService.checkMangaAuthorize(mangaId);
        deleteKeywordOfManga(mangaId);

        Manga manga = mangaService.getMangaById(mangaId);
        if (keywords != null && !keywords.isEmpty()) {
            keywords.forEach(keyword -> {
                Keyword temp = new Keyword();
                temp.setName(keyword);
                temp.setManga(manga);
                KeywordId keywordId = new KeywordId(keyword, manga);
                if (!keywordRepository.existsById(keywordId)) {
                    keywordRepository.save(temp);
                }
            });
        }
        if (manga.getCoverImageUrl() != null) {
            manga.setCoverImageUrl(serverName + manga.getCoverImageUrl());
        }
        return manga;
    }

    @Override
    @Transactional
    public Keyword changeKeywordName(ChangeKeywordVM vm) {

        Manga manga = mangaService.getMangaById(vm.getMangaId());

        KeywordId id = new KeywordId(vm.getName(), manga);
        if (!keywordRepository.existsById(id)) {
            log.error("Keyword does not exist");
            throw new ResourceNotFoundException("Keyword does not exist");
        }
        log.info("Create new keyword id used to check exist when update.");
        KeywordId newId = new KeywordId(vm.getNewName(), id.getManga());
        if (keywordRepository.existsById(newId)) {
            log.error("New keyword is already exist.");
            throw new DataAlreadyExistsException("This keyword name has been existed from database");
        }
        log.info("Getting old keyword from database.......");
        keywordRepository.deleteById(id);
        Keyword keyword = new Keyword();
        keyword.setName(vm.getNewName());
        keyword.setManga(manga);
//        Keyword keyword = getKeywordByKeywordId(vm.getName(), vm.getMangaId());
//        keyword.setName(vm.getNewName());
//        log.info("Saving new keyword to database");
        return keywordRepository.save(keyword);
    }

    @Override
    @Transactional
    public void deleteKeyword(KeywordId id) {
        keywordRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteKeyword(String name, Long mangaId) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Name is null or blank.");
        }
        Manga manga = mangaService.getMangaById(mangaId);
        deleteKeyword(new KeywordId(name, manga));
    }

    @Override
    public void deleteKeyword(String name, String mangaId) {
        Long mangaIdNum = APIUtil.parseStringToLong(mangaId, "mangaId is not a number.");
        deleteKeyword(name, mangaIdNum);
    }

    @Override
    @Transactional
    public void deleteKeywordOfManga(Long id) {
//        keywordRepository.deleteAllKeywordOfManga(id);
        keywordRepository.deleteByMangaId(id);
    }
}
