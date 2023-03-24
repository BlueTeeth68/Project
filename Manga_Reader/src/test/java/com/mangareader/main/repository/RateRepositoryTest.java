package com.mangareader.main.repository;

import com.mangareader.repository.RateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class RateRepositoryTest {

    @Autowired
    private RateRepository rateRepository;

    @Test
    @DisplayName("Test countTotalRate case 1")
    void countTotalRateShouldNotReturnNull1() {
        Integer total = rateRepository.countTotalRate(6L);
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
        assertEquals(8, total);
    }
}