package com.kravchenko.reactive.movie.review.repository;

import com.kravchenko.reactive.movie.review.model.Movie;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface MovieRepository  extends ReactiveCrudRepository<Movie, Long> {
    Flux<Movie> findByGenre(String genre);
}
