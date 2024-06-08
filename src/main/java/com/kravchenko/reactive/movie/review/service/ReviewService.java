package com.kravchenko.reactive.movie.review.service;

import com.kravchenko.reactive.movie.review.model.Review;
import com.kravchenko.reactive.movie.review.repository.MovieRepository;
import com.kravchenko.reactive.movie.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReviewService {

    private static final String MOVIE_WITH_SUCH_ID_NOT_FOUND_MSG = "Movie with such id not found";
    private static final String REVIEW_WITH_SUCH_ID_NOT_FOUND_MSG = "Review with such id not found";

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;

    public ReviewService(ReviewRepository reviewRepository, MovieRepository movieRepository) {
        this.reviewRepository = reviewRepository;
        this.movieRepository = movieRepository;
    }

    public Flux<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Flux<Review> getReviewsByMovieId(Long movieId) {
        return reviewRepository.findByMovieId(movieId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(MOVIE_WITH_SUCH_ID_NOT_FOUND_MSG)));
    }

    public Mono<Review> createReview(Long movieId, Review review) {
        return movieRepository.findById(movieId)
                .flatMap(movie -> {
                    review.setMovieId(movieId);
                    return reviewRepository.save(review)
                            .flatMap(savedReview -> updateMovieRating(movieId)
                                    .then(Mono.just(savedReview)));
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException(MOVIE_WITH_SUCH_ID_NOT_FOUND_MSG)));
    }

    private Mono<Void> updateMovieRating(Long movieId) {
        return reviewRepository.findByMovieId(movieId)
                .collectList()
                .flatMap(reviews -> {
                    double averageRating = reviews.stream()
                            .mapToInt(Review::getRating)
                            .average()
                            .orElse(0.0);
                    return movieRepository.findById(movieId)
                            .flatMap(movie -> {
                                movie.setRating(averageRating);
                                return movieRepository.save(movie);
                            })
                            .then();
                });
    }

    public Mono<Review> updateReview(Long id, Review review) {
        return reviewRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(REVIEW_WITH_SUCH_ID_NOT_FOUND_MSG)))
                .flatMap(existingReview -> {
                    existingReview.setReviewText(review.getReviewText());
                    existingReview.setRating(review.getRating());
                    existingReview.setReviewDate(review.getReviewDate());
                    return reviewRepository.save(existingReview)
                            .flatMap(updatedReview -> updateMovieRating(existingReview.getMovieId())
                                    .then(Mono.just(updatedReview)));
                });
    }

    public Mono<Void> deleteReview(Long id) {
        return reviewRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(REVIEW_WITH_SUCH_ID_NOT_FOUND_MSG)))
                .flatMap(existingReview -> reviewRepository.deleteById(id)
                        .then(updateMovieRating(existingReview.getMovieId())));
    }
}
