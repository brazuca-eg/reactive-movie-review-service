package com.kravchenko.reactive.movie.review.controller;

import com.kravchenko.reactive.movie.review.model.Review;
import com.kravchenko.reactive.movie.review.service.ReviewService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public Flux<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/movie/{movieId}")
    public Flux<Review> getReviewsByMovieId(@PathVariable Long movieId) {
        return reviewService.getReviewsByMovieId(movieId);
    }


    @PostMapping("/movie/{movieId}")
    public Mono<Review> createReview(@PathVariable Long movieId, @RequestBody Review review) {
        return reviewService.createReview(movieId, review);
    }

    @PutMapping("/{id}")
    public Mono<Review> updateReview(@PathVariable Long id, @RequestBody Review review) {
        return reviewService.updateReview(id, review);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteReview(@PathVariable Long id) {
        return reviewService.deleteReview(id);
    }
}
