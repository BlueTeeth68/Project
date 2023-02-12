package com.mangareader.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chapter_image")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChapterImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 0, max = 80)
    @Column(name = "image_url", length = 80)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;
}
