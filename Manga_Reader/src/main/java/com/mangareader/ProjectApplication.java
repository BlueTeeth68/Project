package com.mangareader;

import com.mangareader.domain.*;
import com.mangareader.service.*;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@SuppressWarnings("unused")
@SecurityScheme(
        name = "authorize",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@OpenAPIDefinition(
        info = @Info(title = "Manga reader website", version = "1.0", description = "Swagger UI for testing APIs in Manga reader website.")
)
public class ProjectApplication {

    public ProjectApplication() {
    }

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
            user.setUsername("TestTranslator1");
            user.setDisplayName("Test Translator 1");
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
            Author author;
            User createdBy;

            for (int i = 1; i < 4; i++) {
                author = new Author();
                author.setName("Author " + i);
                createdBy = userService.getUserById(2L);
                author.setUser(createdBy);
                authorService.createAuthor(author);
            }

            for (int i = 4; i < 7; i++) {
                author = new Author();
                author.setName("Author " + i);
                createdBy = userService.getUserById(1L);
                author.setUser(createdBy);
                authorService.createAuthor(author);
            }

            //Add manga to database
            user = userService.getUserById(1L);
            Manga manga = new Manga();
            manga.setUser(user);
            manga.setName("One piece");
            manga.setYearOfPublication(2002);
            mangaService.createManga(manga);

            manga = new Manga();
            manga.setUser(user);
            manga.setName("Naruto");
            manga.setYearOfPublication(2002);
            mangaService.createManga(manga);

            manga = new Manga();
            manga.setUser(user);
            manga.setName("My Hero Academia");
            manga.setYearOfPublication(2008);
            mangaService.createManga(manga);

            user = userService.getUserById(2L);
            manga = new Manga();
            manga.setUser(user);
            manga.setName("Jujutsu Kaisen");
            manga.setYearOfPublication(2016);
            mangaService.createManga(manga);

            manga = new Manga();
            manga.setUser(user);
            manga.setName("Attack on titan");
            manga.setYearOfPublication(2010);
            mangaService.createManga(manga);

            manga = new Manga();
            manga.setUser(user);
            manga.setName("Bleach");
            manga.setYearOfPublication(2004);
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

            keyword = new Keyword();
            keyword.setName("Boruto");
            keyword.setManga(mangaService.getMangaById(2L));
            keywordService.createKeyWord(keyword);

            keyword = new Keyword();
            keyword.setName("Naruto Shippuden");
            keyword.setManga(mangaService.getMangaById(2L));
            keywordService.createKeyWord(keyword);

            //add genre to manga

            /*Set<String> genres = new HashSet<>();
            genres.add(genreService.getGenreById(1L).getName());
            genres.add(genreService.getGenreById(2L).getName());
            genres.add(genreService.getGenreById(3L).getName());
            for (int i = 1; i < 7; i++) {
                manga = mangaService.getMangaById(Long.valueOf(i));
                mangaService.addGenreToManga(manga.getId(), genres, "localhost:8080");
            }*/

            //add authors to manga
            /*Set<Long> authors = new HashSet<>();
            authors.add(1L);
            authors.add(2L);
            authors.add(3L);
            for (int i = 1; i < 4; i++) {
                manga = mangaService.getMangaById(Long.valueOf(i));
                mangaService.addAuthorsToManga(manga.getId(), authors, "localhost:8080");
            }*/
        };
    }
}
