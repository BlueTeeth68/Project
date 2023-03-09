package com.mangareader.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "report")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(columnDefinition = "NVARCHAR(256)")
    private String reason;

    @Column(updatable = false, name = "created_date")
    private LocalDateTime createdDay = LocalDateTime.now();

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
    @JsonIgnoreProperties(value = {"user", "genres", "authors"}, allowSetters = true)
    private Manga manga;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
