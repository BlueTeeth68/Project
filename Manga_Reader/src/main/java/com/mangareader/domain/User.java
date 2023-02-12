package com.mangareader.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Email
    @Size(min = 5, max = 50)
    @Column(length = 50, unique = true)
    private String email;

    @JsonIgnore
    @Size(min = 8, max = 80)
    @Column(length = 80)
    private String password;

    @Size(min = 1, max = 80)
    @Column(length = 80)
    private String google_id;

    @Size(min = 1, max = 80)
    @Column(length = 80)
    private String facebook_id;

    //need to set default avatar, not allow to null
    @Size(max = 80)
    @Column(nullable = false)
    private String avatar_url;

    @NotNull
    @Column(columnDefinition = "bit")
    private Boolean activate = true;

    @Column(updatable = false)
    private Instant createdDate = Instant.now();

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

}
