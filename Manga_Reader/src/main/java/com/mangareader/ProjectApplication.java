package com.mangareader;

import com.mangareader.domain.*;
import com.mangareader.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@SuppressWarnings("unused")
public class ProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @Bean
    CommandLineRunner run(
            IUserService userService,
            IGenreService genreService,
            IAuthorService authorService,
            IMangaService mangaService,
            IKeywordService keywordService
    ) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String password = passwordEncoder.encode("0000");

        return args -> {

            //Add user to database
            User user = new User();
            user.setUsername("SystemAdmin");
            user.setDisplayName("System Admin");
            user.setPassword(password);
            user.setRole(RoleName.ADMIN);
            userService.createUser(user);

            user = new User();
            user.setUsername("TestTranslator");
            user.setDisplayName("Test Translator");
            user.setPassword(password);
            user.setRole(RoleName.TRANSLATOR);
            userService.createUser(user);

            user = new User();
            user.setUsername("TestTranslator2");
            user.setDisplayName("Test Translator 2");
            user.setPassword(password);
            user.setRole(RoleName.TRANSLATOR);
            userService.createUser(user);

            User commonUser = new User();
            commonUser.setUsername("User");
            commonUser.setDisplayName("Common User");
            commonUser.setPassword(password);
            commonUser.setRole(RoleName.USER);
            userService.createUser(commonUser);

            for (int i = 0; i < 10; i++) {
                commonUser = new User();
                commonUser.setUsername("User" + (i + 1));
                commonUser.setDisplayName("Common User " + (i + 1));
                commonUser.setPassword(password);
                commonUser.setRole(RoleName.USER);
                userService.createUser(commonUser);
            }

            //add genres to database
            Genre genre = new Genre();
            genre.setName("Action");
            genreService.createNewGenre(genre);

            genre = new Genre();
            genre.setName("Shounen");
            genreService.createNewGenre(genre);

            genre = new Genre();
            genre.setName("Romance");
            genreService.createNewGenre(genre);

            genre = new Genre();
            genre.setName("Fiction");
            genreService.createNewGenre(genre);

            genre = new Genre();
            genre.setName("Fantastic");
            genreService.createNewGenre(genre);

            genre = new Genre();
            genre.setName("Scientific");
            genreService.createNewGenre(genre);

            //add author to database
            Author author = new Author();
            author.setName("Author 1");
            User createdBy = userService.getUserById(2L);
            author.setUser(createdBy);
            authorService.createAuthor(author);

            author = new Author();
            author.setName("Author 2");
            createdBy = userService.getUserById(2L);
            author.setUser(createdBy);
            authorService.createAuthor(author);

            author = new Author();
            author.setName("Author 3");
            createdBy = userService.getUserById(2L);
            author.setUser(createdBy);
            authorService.createAuthor(author);

            //Add manga to database
            Manga manga = new Manga();
            manga.setName("One piece");
            manga.setYearOfPublication(2011);
            mangaService.createManga(manga);

            manga = new Manga();
            manga.setName("Naruto");
            manga.setYearOfPublication(2002);
            mangaService.createManga(manga);

            manga = new Manga();
            manga.setName("My Hero Academia");
            manga.setYearOfPublication(2006);
            mangaService.createManga(manga);

            // add keyword to manga
            manga = mangaService.getMangaById(1L);
            Keyword keyword = new Keyword();
            keyword.setName("OP");
            keyword.setManga(manga);
            keywordService.createKeyWord(keyword);

            keyword = new Keyword();
            keyword.setName("Đảo Hải Tặc");
            keyword.setManga(manga);
            keywordService.createKeyWord(keyword);
        };
    }

}
