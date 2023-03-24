package com.mangareader.service;

import com.mangareader.domain.Rate;
import com.mangareader.domain.RateId;
import com.mangareader.web.rest.vm.RateVM;

@SuppressWarnings("unused")
public interface IRateService {

    Rate getRateById(RateId id);

    Rate rateManga(RateVM vm);

    Rate getUserRate(Long mangaId);

}
