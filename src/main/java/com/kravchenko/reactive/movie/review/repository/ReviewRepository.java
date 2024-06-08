package com.kravchenko.reactive.movie.review.repository;

import com.kravchenko.reactive.movie.review.model.Review;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReviewRepository extends ReactiveCrudRepository<Review, Long> {
    Flux<Review> findByMovieId(Long movieId);

    Mono<Void> deleteByMovieId(Long movieId);
}