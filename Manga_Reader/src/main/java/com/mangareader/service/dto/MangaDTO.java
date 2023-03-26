package com.mangareader.service.dto;

import com.mangareader.domain.MangaStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MangaDTO {

    private Long id;

    private String name;

    private String coverImageUrl;

    private Integer view;

    private String summary;

    private Float rate;

    private Integer totalVote;

    private MangaStatus status;

    private LocalDateTime latestUpdate;

    private Integer yearOfPublication;

    private Long user_id;

    private Set<String> genres = new HashSet<>();

    private Set<AuthorDTO> authors = new HashSet<>();

    private List<ChapterDTO> chapters = new ArrayList<>();
}
