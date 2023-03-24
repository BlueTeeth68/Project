package com.mangareader.service.impl;

import com.mangareader.domain.Manga;
import com.mangareader.domain.Rate;
import com.mangareader.domain.RateId;
import com.mangareader.domain.User;
import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.repository.RateRepository;
import com.mangareader.service.IMangaService;
import com.mangareader.service.IRateService;
import com.mangareader.service.IUserService;
import com.mangareader.web.rest.vm.RateVM;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateServiceImpl implements IRateService {

    private final RateRepository rateRepository;

    private final IUserService userService;

    private final IMangaService mangaService;

    @Override
    public Rate getRateById(RateId id) {
        return rateRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Rate does not exists.")
        );
    }

    @Override
    @Transactional
    public Rate rateManga(RateVM vm) {
        User user = userService.getCurrentUser();
        Manga manga = mangaService.getMangaById(vm.getMangaId());
        RateId rateId = new RateId(manga, user);
        Rate rate;
        if (!rateRepository.existsById(rateId)) {
            rate = new Rate();
            rate.setUser(user);
            rate.setManga(manga);
            rate.setPoint(vm.getPoint());
            rate = rateRepository.save(rate);
        } else {
            rate = getRateById(rateId);
            rate.setPoint(vm.getPoint());
            rate = rateRepository.save(rate);
        }
        int numberOfRate = rateRepository.countTotalRate(vm.getMangaId());
        int totalRate = rateRepository.sumTotalRate(vm.getMangaId());
        if (numberOfRate > 0) {
            manga.setRate((float) totalRate / numberOfRate);
        }
        manga.setTotalVote(numberOfRate);
        mangaService.saveManga(manga);
        return rate;
    }

    @Override
    public Rate getUserRate(Long mangaId) {
        User user = userService.getCurrentUser();
        Manga manga = mangaService.getMangaById(mangaId);
        RateId rateId = new RateId(manga, user);
        return getRateById(rateId);
    }
}
