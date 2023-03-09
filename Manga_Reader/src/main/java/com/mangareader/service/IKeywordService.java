package com.mangareader.service;

import com.mangareader.domain.Keyword;
import com.mangareader.domain.KeywordId;
import com.mangareader.domain.Manga;
import com.mangareader.web.rest.vm.ChangeKeywordVM;

import java.util.List;

public interface IKeywordService {

    Keyword createKeyWord(Keyword keyword);

    Keyword getKeywordByKeywordId(String name, Long manga_id);

    List<Keyword> getKeywordsOfMangaSortedByName(Long mangaId);

    List<Manga> getMangaByKeyword(String name);

    Manga addKeywordToManga(Long mangaId, List<String> keywords);

    Keyword changeKeywordName(ChangeKeywordVM vm);

    void deleteKeyword(KeywordId id);

    void deleteKeyword(String name, Long mangaId);

    void deleteKeywordOfManga(Long id);

}
