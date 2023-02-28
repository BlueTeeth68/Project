package com.mangareader.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "author")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @JsonIgnore
    @ManyToMany(mappedBy = "authors")
    @JsonIgnoreProperties(value = "authors", allowSetters = true)
    private Set<Manga> mangas = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "created_by", updatable = false)
    private User user;
}
