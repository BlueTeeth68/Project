package com.mangareader.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "keyword")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@IdClass(KeywordId.class)
public class Keyword {
    @Id
    @Column(nullable = false)
    private String name;

    @JsonIgnore
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manga_id", nullable = false)
//    @JsonIgnoreProperties(value = {"user", "genres", "authors", "comments", "reports",
//            "keywords", "bookmarks", "chapters"}, allowSetters = true)
    private Manga manga;
}
