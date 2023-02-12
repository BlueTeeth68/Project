package com.mangareader.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "reply-comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotBlank
    @Column(columnDefinition = "text")
    private String content;

    @Column(name = "is_change", columnDefinition = "bit")
    private Boolean isChange = false;

    @Column(name = "created_date")
    private Instant createdDate = Instant.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;
}
