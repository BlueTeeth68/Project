package com.mangareader.service;

import com.mangareader.domain.Keyword;
import com.mangareader.domain.KeywordId;
import com.mangareader.domain.Manga;
import com.mangareader.web.rest.vm.ChangeKeywordVM;

import java.util.List;

@SuppressWarnings("unused")
public interface IKeywordService {

    Keyword createKeyWord(Keyword keyword);

    Keyword getKeywordByKeywordId(String name, Long mangaId);

    Keyword getKeywordByKeywordId(String name, String mangaId);

    List<Keyword> getKeywordsOfMangaSortedByName(Long mangaId);

    List<Keyword> getKeywordsOfMangaSortedByName(String mangaId);

/*    List<Manga> getMangaByKeyword(String name);*/

    Manga addKeywordToManga(Long mangaId, List<String> keywords);

    Keyword changeKeywordName(ChangeKeywordVM vm);

    void deleteKeyword(KeywordId id);

    void deleteKeyword(String name, Long mangaId);

    void deleteKeyword(String name, String mangaId);

    void deleteKeywordOfManga(Long id);

}
