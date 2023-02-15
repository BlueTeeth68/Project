package com.mangareader.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "keyword")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(KeywordId.class)
public class Keyword {

    /*
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    */

    @Id
    @Column(nullable = false)
    private String name;

    @Id
    @ManyToOne()
    @JoinColumn(name = "manga_id", nullable = false)
    private Manga manga;
}
