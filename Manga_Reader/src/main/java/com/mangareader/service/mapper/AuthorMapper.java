package com.mangareader.service.mapper;

import com.mangareader.domain.Author;
import com.mangareader.service.IAuthorService;
import com.mangareader.service.dto.AuthorDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorMapper {

    private final IAuthorService authorService;

    public AuthorDTO toDTO(Author input) {
        AuthorDTO result = new AuthorDTO();
        result.setId(input.getId());
        result.setName(input.getName());
        return result;
    }

    public Author toEntity(AuthorDTO input) {
        if (input.getId() == null)
            return null;
        Author result = authorService.getAuthorById(input.getId());
        return result;
    }

}
