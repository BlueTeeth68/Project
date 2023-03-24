package com.mangareader.repository;

import com.mangareader.domain.Rate;
import com.mangareader.domain.RateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RateRepository extends JpaRepository<Rate, RateId> {

    @Query(" SELECT COUNT(*) FROM Rate r " +
            " WHERE r.manga.id = :mangaId ")
    Integer countTotalRate(@Param("mangaId") Long mangaId);

    @Query(" SELECT IFNULL(SUM(r.point), 0) FROM Rate r " +
            " WHERE r.manga.id = :mangaId ")
    Integer sumTotalRate(@Param("mangaId") Long mangaId);

}
