package com.mangareader.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genre")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 50, nullable = false, unique = true)
    private String name;

    //need to remove because genres save a lot of mangas, when load genre, manga also be loaded
    // => low performance
    //* Can use Fetch LAZY
    @JsonIgnore
    @ManyToMany(mappedBy = "genres", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties(value = {"user", "genres", "authors"}, allowSetters = true)
    private Set<Manga> mangas = new HashSet<>();

}
