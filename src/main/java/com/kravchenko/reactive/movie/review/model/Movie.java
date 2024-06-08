package com.kravchenko.reactive.movie.review.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@Table("movies")
public class Movie {
    @Id
    private Long id;
    private String title;
    private String genre;
    private LocalDate releaseDate;
    private String director;
    private Double rating;
}
