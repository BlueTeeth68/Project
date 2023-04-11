package com.mangareader.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "`user`")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true)
    private String username;

    @JsonIgnore
    @Column(length = 80, name = "`password`")
    private String password;

    @Size(min = 1, max = 80)
    @Column(name = "google_id", length = 80)
    private String googleId;

    @Size(min = 1, max = 80)
    @Column(name = "facebook_id", length = 80)
    private String facebookId;

    @Size(max = 50)
    @NotBlank
    @Column(name = "display_name", columnDefinition = "NVARCHAR(50)", unique = true)
    private String displayName;

    @Size(min = 1, max = 250)
    @Column(name = "avatar_url", columnDefinition = "NVARCHAR(250)")
    private String avatarUrl;

    @Column(columnDefinition = "bit", name = "`activate`")
    private Boolean activate = true;

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "`role`", columnDefinition = "VARCHAR(20)", nullable = false)
    private RoleName role;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST})
    private Set<Manga> mangas = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST})
    private Set<Author> authors = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private Set<History> histories = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private Set<Notification> notifications = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private Set<Bookmark> bookmarks = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private Set<Rate> rates = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private Set<Comment> comments = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private Set<ReplyComment> replyComments = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private Set<Report> reports = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "reporter", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private Set<Report> reported = new HashSet<>();

    @PreRemove
    void collapseForeignKey() {
        mangas.forEach(
                manga -> manga.setUser(null)
        );

        authors.forEach(
                author -> author.setUser(null)
        );
    }
}
