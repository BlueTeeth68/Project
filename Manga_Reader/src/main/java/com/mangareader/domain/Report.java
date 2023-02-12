package com.mangareader.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "report")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String reason;

    @Column(updatable = false)
    private Instant createdDay = Instant.now();

    @Column(columnDefinition = "bit")
    private Boolean status = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @ManyToOne
    @JoinColumn(name = "manga_id")
    private Manga manga;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
