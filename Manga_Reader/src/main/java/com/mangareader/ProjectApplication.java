package com.mangareader;

import com.mangareader.domain.*;
import com.mangareader.service.*;
import com.mangareader.web.rest.vm.CreateCommentVM;
import com.mangareader.web.rest.vm.RateVM;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
            IKeywordService keywordService,
            IRateService rateService,
            ICommentService commentService
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
            genreService.createNewGenre("Action");

            genreService.createNewGenre("Shounen");

            genreService.createNewGenre("Romance");

            genreService.createNewGenre("Fiction");

            genreService.createNewGenre("Fantastic");

            genreService.createNewGenre("Scientific");

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

            //Set login user
            User currentUser = userService.getUserById(1L);

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    currentUser.getUsername(),
                    null,  //credential
                    authorities// authorities
            );

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authToken);
            SecurityContextHolder.setContext(context);
            //set login user

            //add genre to manga

            Set<String> genres = new HashSet<>();
            genres.add(genreService.getGenreById(1L).getName());
            genres.add(genreService.getGenreById(2L).getName());
            genres.add(genreService.getGenreById(3L).getName());
            for (int i = 1; i < 7; i++) {
                manga = mangaService.getMangaById(Long.valueOf(i));
                mangaService.addGenreToManga(manga.getId(), genres);
            }

            //add authors to manga
            Set<Long> authors = new HashSet<>();
            authors.add(1L);
            authors.add(2L);
            authors.add(3L);
            for (int i = 1; i < 4; i++) {
                manga = mangaService.getMangaById(Long.valueOf(i));
                mangaService.addAuthorsToManga(manga.getId(), authors);
            }

            //add rate to manga
            RateVM rateVM = new RateVM(10, 3L);
            rateService.rateManga(rateVM);

            rateVM = new RateVM(8, 1L);
            rateService.rateManga(rateVM);

            rateVM = new RateVM(9, 2L);
            rateService.rateManga(rateVM);

            rateVM = new RateVM(10, 5L);
            rateService.rateManga(rateVM);
            //add rate to manga


            //create some comment

            String content = "This is the most interesting manga that I've read.";
            String content2 = "I love th is manga.";
            String content3 = "Interesting";
            commentService.createNewComment(new CreateCommentVM(3L, content));
            commentService.createNewComment(new CreateCommentVM(3L, content2));
            commentService.createNewComment(new CreateCommentVM(3L, content3));
            commentService.createNewComment(new CreateCommentVM(1L, content));
            commentService.createNewComment(new CreateCommentVM(2L, content));
            commentService.createNewComment(new CreateCommentVM(4L, content));

            //create some comment

            //create some reply comment
            String replyContent1 = "OK";
            String replyContent2 = "I love this too.";
            String replyContent3 = "beg" + "laughter" + "ceremony" + "priest";
            commentService.createReplyComment(new CreateCommentVM(1L, replyContent1));
            commentService.createReplyComment(new CreateCommentVM(1L, replyContent2));
            commentService.createReplyComment(new CreateCommentVM(2L, replyContent1));
            commentService.createReplyComment(new CreateCommentVM(3L, replyContent3));
            commentService.createReplyComment(new CreateCommentVM(3L, replyContent1));
            //create some reply comment

            //Clear security context
            SecurityContextHolder.clearContext();

        };
    }
}
