package com.mangareader.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "cover_image_url", columnDefinition = "NVARCHAR(100)")
    private String coverImageUrl;

    @Min(0)
    @Column(name = "view")
    private Integer view = 0;

    @Min(0)
    @Column(name = "avg_view")
    private Float avgView = 0F;

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

    @NotNull
    @Min(1900)
    @Max(2100)
    @Column(name = "year_of_publication", nullable = false)
    private Integer yearOfPublication;

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id"/*, updatable = false*/)
    private User user;

    @ManyToMany(fetch = FetchType.EAGER/*, cascade = CascadeType.REMOVE*/)
    @JoinTable(name = "manga_genre",
            joinColumns = @JoinColumn(name = "manga_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @JsonIgnoreProperties(value = "mangas", allowSetters = true)
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER/*, cascade = CascadeType.REMOVE*/)
    @JoinTable(name = "manga_author",
            joinColumns = @JoinColumn(name = "manga_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @JsonIgnoreProperties(value = {"mangas", "created_by"}, allowSetters = true)
    private Set<Author> authors = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "manga",
            cascade = {CascadeType.REMOVE, CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    private Set<Chapter> chapters = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "manga",
            cascade = {CascadeType.REMOVE, CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    private Set<Bookmark> bookmarks = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "manga",
            cascade = {CascadeType.REMOVE, CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    private Set<Rate> rates = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "manga",
            cascade = {CascadeType.REMOVE, CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    private Set<Keyword> keywords = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "manga",
            cascade = {CascadeType.REMOVE, CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "manga",
            cascade = {CascadeType.REMOVE, CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    private Set<Report> reports = new HashSet<>();

}
