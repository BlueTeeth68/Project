package com.mangareader.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "manga")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(min = 1, max = 256)
    @Column(name = "cover_image_url", length = 80)
    private String coverImageUrl;

    @Min(0)
    @Column(name = "view")
    private Integer view = 0;

    @Column(name = "sumary", columnDefinition = "text")
    private String sumary;

    @Min(0)
    @Max(10)
    @Column(name = "rate")
    private Float rate = 0F;

    @Min(0)
    @Column(name = "total_vote")
    private Integer totalVote = 0;

    @Column(name = "status")
    private MangaStatus status = MangaStatus.Ongoing;

    @Column(name = "lastest_update")
    private Instant lastestUpdate;

    @Min(1900)
    @Max(2100)
    @Column(name = "year_of_publication")
    private Integer yearOfPublication;

    @Column(name = "created_Date", updatable = false)
    private Instant createdDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(name = "manga_genre",
            joinColumns = @JoinColumn(name = "manga_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "manga_author",
            joinColumns = @JoinColumn(name = "manga_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();

}
