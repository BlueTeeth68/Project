package com.mangareader.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(columnDefinition = "text")
    private String content;

    @Column(name = "is_read", columnDefinition = "bit")
    private Boolean isRead = false;

    @Column(updatable = false)
    private Instant date = Instant.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
