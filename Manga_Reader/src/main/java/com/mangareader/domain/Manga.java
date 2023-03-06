package com.mangareader.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "manga")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Manga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "name", nullable = false, columnDefinition = "NVARCHAR(256)")
    private String name;

    @Lob
    @NotNull
    @Column(name = "cover_image_url", columnDefinition = "NVARCHAR(100)", nullable = false)
    private String coverImageUrl;

    @Min(0)
    @Column(name = "view")
    private Integer view = 0;

    @Size(max = 1000)
    @Column(name = "summary", columnDefinition = "NVARCHAR(1000)")
    private String summary;

    @Min(0)
    @Max(10)
    @Column(name = "rate")
    private Float rate = 0F;

    @Min(0)
    @Column(name = "total_vote")
    private Integer totalVote = 0;

    @Column(name = "status", columnDefinition = "NVARCHAR(20)")
    @Enumerated(EnumType.STRING)
    private MangaStatus status = MangaStatus.Ongoing;

    @Column(name = "latest_update")
    private LocalDateTime latestUpdate = LocalDateTime.now();

    @Min(1900)
    @Max(2100)
    @Column(name = "year_of_publication")
    private Integer yearOfPublication;

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "manga_genre",
            joinColumns = @JoinColumn(name = "manga_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @JsonIgnoreProperties(value = "mangas", allowSetters = true)
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "manga_author",
            joinColumns = @JoinColumn(name = "manga_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @JsonIgnoreProperties(value = "mangas", allowSetters = true)
    private Set<Author> authors = new HashSet<>();

}
