package com.kravchenko.reactive.movie.review.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@Table("reviews")
public class Review {
    @Id
    private Long id;
    @Column("movie_id")
    private Long movieId;
    private String reviewText;
    private Integer rating;
    private LocalDate reviewDate;
}
