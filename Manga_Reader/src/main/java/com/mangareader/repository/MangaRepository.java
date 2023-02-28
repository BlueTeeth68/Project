package com.mangareader.repository;

import com.mangareader.domain.Manga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MangaRepository extends JpaRepository<Manga, Long> {

    public List<Manga> findAllOrderByLatestUpdateDesc();

}
