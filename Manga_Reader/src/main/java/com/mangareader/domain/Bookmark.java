package com.mangareader.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "bookmark")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne()
    @JoinColumn(name = "manga_id", nullable = false)
    @JsonIgnoreProperties(value = {"user", "genres", "authors"}, allowSetters = true)
    private Manga manga;
}
