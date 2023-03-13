package com.mangareader.main.service.impl;

import com.mangareader.domain.Author;
import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.exception.BadRequestException;
import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.repository.AuthorRepository;
import com.mangareader.service.IUserService;
import com.mangareader.service.impl.AuthorServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private IUserService userService;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private User user;

    @BeforeEach
    void setup() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode("0000");

        user = new User();
        user.setId(1L);
        user.setUsername("SystemAdmin");
        user.setDisplayName("System Admin");
        user.setPassword(password);
        user.setRole(RoleName.ADMIN);
    }


    @Test
    @DisplayName("Test createAuthor case 1")
    void createAuthorShouldCreateAndReturnAnAuthor() {
        //given

        Author author = new Author();
        author.setName("Author 1");
        author.setUser(user);

        //when
        authorService.createAuthor(author);

        //then
        verify(authorRepository).save(author);
    }

    @Test
    @DisplayName("Test createAuthor case 2")
    void createAuthorShouldReturnAStudent() {
        //given
        Author author = new Author();
        author.setName("Author 1");
        author.setUser(user);

        //when
        authorService.createAuthor(author);

        //then
        ArgumentCaptor<Author> authorArgumentCaptor = ArgumentCaptor.forClass(Author.class);
        verify(authorRepository).save(authorArgumentCaptor.capture());
        Author authorCaptor = authorArgumentCaptor.getValue();
        assertThat(authorCaptor).isEqualTo(author);
    }

    @Test
    @DisplayName("Test createAuthor case 3")
    void createAuthorShouldThrowException() {
        //given
        Author author = new Author();
        author.setId(1L);
        author.setName("Author 1");
        author.setUser(user);

        //when, then
        assertThatThrownBy(() -> {
                    authorService.createAuthor(author);
                }
        )
                .hasMessage("Author can not already have an id")
                .isInstanceOf(BadRequestException.class);
        verify(authorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test common createAuthor case 1")
    void commonCreateAuthorShouldCallCreateAuthorFunction() {
        //given
        String name = "Author 1";
        Long userId = 1L;

        Author author = new Author();
        author.setName(name);
        author.setUser(user);

        given(userService.getUserById(anyLong())).willReturn(user);
        given(authorRepository.save(any(Author.class))).willReturn(author);
        //when
        Author createdAuthor = authorService.createAuthor(name, userId);
        //then

        assertNotNull(createdAuthor);
        assertEquals(name, createdAuthor.getName());
        assertEquals(userId, createdAuthor.getUser().getId());

        verify(userService).getUserById(userId);
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    @DisplayName("Test common createAuthor case 2")
    void commonCreateAuthorMustThrowExceptionWhenNameIsNull() {
        //given
        String name = null;

        //when
        //then
        assertThatThrownBy(() -> {
            authorService.createAuthor(name, 1L);
        })
                .hasMessage("name is null or blank")
                .isInstanceOf(BadRequestException.class);

        verify(userService, never()).getUserById(anyLong());
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    @DisplayName("Test common createAuthor case 3")
    void commonCreateAuthorMustThrowExceptionWhenNameIsBlank() {
        //given
        String name = "     ";

        //when
        //then
        assertThatThrownBy(() -> {
            authorService.createAuthor(name, 1L);
        })
                .hasMessage("name is null or blank")
                .isInstanceOf(BadRequestException.class);

        verify(userService, never()).getUserById(anyLong());
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    @DisplayName("test getAllAuthor case 1")
    void getAllAuthorShouldReturnAList() {
        //given
        List<Author> authors = new ArrayList<>();
        authors.add(new Author());

        given(authorRepository.findAll()).willReturn(authors);
        //when
        List<Author> result = authorService.getAllAuthor();
        //then

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(authorRepository).findAll();
    }

    @Test
    @DisplayName("test getAllAuthor case 2")
    void getAllAuthorShouldThrowException() {
        //given
        List<Author> authors = new ArrayList<>();
        given(authorRepository.findAll()).willReturn(authors);
        //when
        //then

        assertThatThrownBy(() -> {
            authorService.getAllAuthor();
        })
                .hasMessage("There are no author in the database.")
                .isInstanceOf(ResourceNotFoundException.class);

        verify(authorRepository).findAll();
    }

    @Test
    @DisplayName("test getAuthorById case 1")
    void getAuthorByIdMustReturnAuthor() {
        //given
        Author author = new Author();
        author.setName("Author 1");
        given(authorRepository.findById(anyLong())).willReturn(Optional.of(author));
        //when
        Author result = authorService.getAuthorById(1L);
        //then

        assertNotNull(result);
        assertEquals(author, result);

        verify(authorRepository).findById(anyLong());
    }

    @Test
    @DisplayName("test getAuthorById case 2")
    void getAuthorByIdMustThrowException() {
        //given
        given(authorRepository.findById(anyLong())).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> {
            authorService.getAuthorById(1L);
        })
                .hasMessage("There are no author " + 1L + " in the database")
                .isInstanceOf(ResourceNotFoundException.class);
        verify(authorRepository).findById(anyLong());
    }


}
