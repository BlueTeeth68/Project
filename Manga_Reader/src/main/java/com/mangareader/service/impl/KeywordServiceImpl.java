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
        Keyword result = keywordRepository.save(keyword);
        return result;
    }

    @Override
    public Keyword getKeywordByKeywordId(String name, Long manga_id) {

        if (name == null || name.isBlank()) {
            throw new BadRequestException("Invalid keyword name: null or blank.");
        }
        Manga manga = mangaService.getMangaById(manga_id);

        KeywordId id = new KeywordId(name, manga);
        log.info("Finding keyword by KeywordId");
        Keyword result = keywordRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Keyword name " + id.getName() + " in manga "
                        + id.getManga().getId() + " does not exist.")
        );

        return result;
    }

    //need to testing here
    @Override
    public List<Keyword> getKeywordsOfMangaSortedByName(Long mangaId) {

        log.info("Finding keyword by mangaId........");
        List<Keyword> result = keywordRepository.findByMangaIdOrderByName(mangaId);

        return result;
    }

/*    @Override
    public List<Manga> getMangaByKeyword(String name) {

        if (name == null || name.isEmpty()) {
            throw new BadRequestException("keyword is null or blank.");
        }
        List<Manga> mangaList = keywordRepository.findMangasByKeyword(name);
        return null;
    }*/

    @Override
    @Transactional
    public Manga addKeywordToManga(Long mangaId, List<String> keywords) {
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
        KeywordId newId = new KeywordId(vm.getName(), id.getManga());
        if (keywordRepository.existsById(newId)) {
            log.error("New keyword is already exist.");
            throw new DataAlreadyExistsException("This keyword name has been existed from database");
        }
        log.info("Getting old keyword from database.......");
        Keyword keyword = getKeywordByKeywordId(vm.getName(), vm.getMangaId());
        keyword.setName(vm.getNewName());
        log.info("Saving new keyword to database");
        Keyword result = keywordRepository.save(keyword);

        return result;
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
    @Transactional
    public void deleteKeywordOfManga(Long id) {
//        keywordRepository.deleteAllKeywordOfManga(id);
        keywordRepository.deleteByMangaId(id);
    }
}
