package com.mangareader.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "history")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@IdClass(HistoryId.class)
public class History {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "manga_id")
    @JsonIgnoreProperties(value = {"user", "genres", "authors"}, allowSetters = true)
    private Manga manga;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    @JsonIgnoreProperties(value = "manga", allowSetters = true)
    private Chapter chapter;

    @Column()
    private LocalDateTime date = LocalDateTime.now();
}
