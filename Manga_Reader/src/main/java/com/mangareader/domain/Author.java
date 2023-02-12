package com.mangareader.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "author")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(length = 30)
    private String name;

    @Column(name = "created_date", updatable = false)
    private Instant createdDate = Instant.now();

    @ManyToMany(mappedBy = "authors")
    private Set<Manga> mangas = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "created_by", updatable = false)
    private User user;
}
