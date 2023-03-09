package com.mangareader.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chapter")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    //need to check that a manga can not have two similar chapter
    @Min(0)
    @Column(name = "chapter_number", nullable = false)
    private Float chapterNumber;

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "manga_id", nullable = false)
    @JsonIgnoreProperties(value = {"user", "genres", "authors"}, allowSetters = true)
    private Manga manga;

    @JsonIgnore
    @OneToMany(mappedBy = "chapter", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private Set<ChapterImage> chapterImages = new HashSet<>();
}
