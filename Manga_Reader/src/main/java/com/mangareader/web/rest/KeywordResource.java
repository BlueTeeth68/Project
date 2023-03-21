package com.mangareader.web.rest;

import com.mangareader.domain.Keyword;
import com.mangareader.service.IKeywordService;
import com.mangareader.service.dto.KeywordDTO;
import com.mangareader.service.mapper.KeywordMapper;
import com.mangareader.web.rest.vm.ChangeKeywordVM;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/keyword")
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused")
public class KeywordResource {

    private final IKeywordService keywordService;
    private final KeywordMapper keywordMapper;

    @GetMapping()
    public ResponseEntity<String> getKeywordById(
            @RequestParam String name,
            @RequestParam String mangaId
    ) {
        Keyword result = keywordService.getKeywordByKeywordId(name, mangaId);
        return new ResponseEntity<>(result.getName(), HttpStatus.FOUND);
    }

    @GetMapping("/manga")
    public ResponseEntity<List<String>> getKeywordOfManga(
            @RequestParam String mangaId
    ) {
        List<Keyword> keywords = keywordService.getKeywordsOfMangaSortedByName(mangaId);
        List<String> result = new ArrayList<>();
        keywords.forEach(
                k -> result.add(k.getName())
        );
        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<String> addKeywordToManga(
            @Valid @RequestBody KeywordDTO keywordDTO
    ) {
        Keyword result = keywordMapper.toEntity(keywordDTO);
        result = keywordService.createKeyWord(result);
        return new ResponseEntity<>(result.getName(), HttpStatus.CREATED);
    }

    @PatchMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<String> changeKeywordName(
            @Valid @RequestBody ChangeKeywordVM vm
    ) {
        Keyword result = keywordService.changeKeywordName(vm);
        return new ResponseEntity<>(result.getName(), HttpStatus.OK);
    }

    @DeleteMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN','TRANSLATOR')")
    public ResponseEntity<?> deleteKeyword(
            @RequestParam String name,
            @RequestParam String mangaId
    ) {
        keywordService.deleteKeyword(name, mangaId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
