package com.mangareader.main.repository;

import com.mangareader.domain.Manga;
import com.mangareader.domain.Rate;
import com.mangareader.domain.RoleName;
import com.mangareader.domain.User;
import com.mangareader.repository.MangaRepository;
import com.mangareader.repository.RateRepository;
import com.mangareader.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RateRepositoryTest {

    @Autowired
    private RateRepository rateRepository;
    @Autowired
    private MangaRepository mangaRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void setup() {
        User user = new User();
        user.setUsername("SystemAdmin");
        user.setDisplayName("System Admin");
        user.setRole(RoleName.ADMIN);
        user = userRepository.save(user);

        Manga manga = new Manga();
        manga.setName("Manga 1");
        manga.setUser(user);
        manga.setYearOfPublication(2013);
        mangaRepository.save(manga);

        manga = new Manga();
        manga.setName("Manga 2");
        manga.setUser(user);
        manga.setYearOfPublication(2013);
        mangaRepository.save(manga);

        Rate rate = new Rate();
        rate.setPoint(10);
        rate.setUser(user);
        rate.setManga(mangaRepository.findById(1L).orElse(null));
        rateRepository.save(rate);

    }

    @Test
    @DisplayName("Test countTotalRate case 1")
    void countTotalRateShouldNotReturnNull1() {
        Integer total = rateRepository.countTotalRate(2L);
        assertNotNull(total);
        assertEquals(0, total);
    }

    @Test
    @DisplayName("Test countTotalRate case 2")
    void countTotalRateShouldNotReturnNull2() {
        Integer total = rateRepository.countTotalRate(1L);
        assertNotNull(total);
        assertEquals(1, total);
    }

    @Test
    void sumTotalRateShouldReturnAnInt() {
        Integer total = rateRepository.sumTotalRate(1L);
        assertNotNull(total);
        assertEquals(10, total);
    }
}