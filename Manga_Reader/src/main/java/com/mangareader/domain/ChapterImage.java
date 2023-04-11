package com.mangareader.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "chapter_image")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChapterImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 250)
    @Column(name = "image_url", columnDefinition = "NVARCHAR(250)")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "chapter_id", nullable = false)
    @JsonIgnoreProperties(value = "manga", allowSetters = true)
    private Chapter chapter;
}
