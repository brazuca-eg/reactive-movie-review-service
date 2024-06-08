package com.kravchenko.reactive.movie.review.service;

import com.kravchenko.reactive.movie.review.model.Movie;
import com.kravchenko.reactive.movie.review.repository.MovieRepository;
import com.kravchenko.reactive.movie.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MovieService {

    private static final String MOVIE_NOT_FOUND_MSG = "Movie not found";

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;

    public MovieService(MovieRepository movieRepository, ReviewRepository reviewRepository) {
        this.movieRepository = movieRepository;
        this.reviewRepository = reviewRepository;
    }

    public Flux<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Flux<Movie> getMoviesByGenre(String genre) {
        return movieRepository.findByGenre(genre);
    }

    public Mono<Movie> getMovieById(Long id) {
        return movieRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(MOVIE_NOT_FOUND_MSG)));
    }

    public Mono<Movie> createMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Mono<Movie> updateMovie(Long id, Movie movie) {
        return movieRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(MOVIE_NOT_FOUND_MSG)))
                .flatMap(existingMovie -> {
                    existingMovie.setTitle(movie.getTitle());
                    existingMovie.setGenre(movie.getGenre());
                    existingMovie.setReleaseDate(movie.getReleaseDate());
                    existingMovie.setDirector(movie.getDirector());
                    return movieRepository.save(existingMovie);
                });
    }

    public Mono<Void> deleteMovie(Long id) {
        return movieRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(MOVIE_NOT_FOUND_MSG)))
                .flatMap(existingMovie -> reviewRepository.deleteByMovieId(existingMovie.getId())
                        .then(movieRepository.deleteById(existingMovie.getId())));
    }
}
