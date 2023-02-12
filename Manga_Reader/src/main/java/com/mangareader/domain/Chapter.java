package com.mangareader.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "chapter")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(length = 50, nullable = true)
    private String name;

    //need to check that a manga can not have two similar chapter
    @Min(0)
    @Column(name = "chapter_number", nullable = false)
    private Float chapterNumber;

    @Column(name = "created_date", updatable = false)
    private Instant createdDate = Instant.now();

    @ManyToOne
    @JoinColumn(name = "manga_id")
    private Manga manga;
}
