package com.mangareader.repository;

import com.mangareader.domain.Rate;
import com.mangareader.domain.RateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateRepository extends JpaRepository<Rate, RateId> {
}
