package com.mangareader.web.rest;

import com.mangareader.domain.Keyword;
import com.mangareader.service.IKeywordService;
import com.mangareader.service.dto.KeywordDTO;
import com.mangareader.service.mapper.KeywordMapper;
import com.mangareader.service.util.APIUtil;
import com.mangareader.web.rest.vm.ChangeKeywordVM;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/keyword")
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class KeywordResource {

    private final IKeywordService keywordService;

    private final KeywordMapper keywordMapper;

    @GetMapping()
    public ResponseEntity<Keyword> getKeywordById(
            @RequestParam String name,
            @RequestParam String mangaId
    ) {
        Long mangaIdNum = APIUtil.parseStringToLong(mangaId, "mangaId is not a number exception.");
        Keyword result = keywordService.getKeywordByKeywordId(name, mangaIdNum);

        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @GetMapping("/manga")
    public ResponseEntity<List<Keyword>> getKeywordOfManga(
            @RequestParam String mangaId
    ) {
        Long mangaIdNum = APIUtil.parseStringToLong(mangaId, "MangaId is not a number.");
        List<Keyword> result = keywordService.getKeywordsOfMangaSortedByName(mangaIdNum);

        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<Keyword> addKeywordToManga(
            @Valid @RequestBody KeywordDTO keywordDTO
    ) {
        Keyword result = keywordMapper.toEntity(keywordDTO);
        result = keywordService.createKeyWord(result);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PatchMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<Keyword> changeKeywordName(
            @Valid @RequestBody ChangeKeywordVM vm
    ) {
        Keyword result = keywordService.changeKeywordName(vm);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<?> deleteKeyword(
            @RequestParam String name,
            @RequestParam String mangaId
    ) {
        Long mangaIdNum = APIUtil.parseStringToLong(mangaId, "mangaId is not a number.");
        keywordService.deleteKeyword(name, mangaIdNum);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
