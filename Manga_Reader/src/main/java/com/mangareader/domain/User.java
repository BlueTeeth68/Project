package com.mangareader.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true)
    private String username;

    @JsonIgnore
    @Size(min = 8, max = 80)
    @Column(length = 80)
    private String password;

    @Size(min = 1, max = 80)
    @Column(name = "google_id", length = 80)
    private String googleId;

    @Size(min = 1, max = 80)
    @Column(name = "facebook_id", length = 80)
    private String facebookId;

    @Size(max = 50)
    @Column(name = "display_name", columnDefinition = "NVARCHAR(50)", unique = true)
    private String displayName;

    @Lob
    @Column()
    private byte[] avatar;

    /*    @NotNull*/
    @Column(columnDefinition = "bit")
    private Boolean activate = true;

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

/*    @JsonIgnore*/
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();

}
