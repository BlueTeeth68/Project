package com.mangareader.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chapter", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"chapter_number", "manga_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    //need to check that a manga can not have two similar chapter
    @NotNull
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
    private List<ChapterImage> chapterImages = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "chapter", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<ChapterImage> histories = new ArrayList<>();
}
