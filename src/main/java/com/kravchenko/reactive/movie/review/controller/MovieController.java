package com.kravchenko.reactive.movie.review.controller;

import com.kravchenko.reactive.movie.review.model.Movie;
import com.kravchenko.reactive.movie.review.service.MovieService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public Flux<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/genre")
    public Flux<Movie> getMoviesByGenre(@RequestParam String genre) {
        return movieService.getMoviesByGenre(genre);
    }

    @GetMapping("/{id}")
    public Mono<Movie> getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    @PostMapping
    public Mono<Movie> createMovie(@RequestBody Movie movie) {
        return movieService.createMovie(movie);
    }

    @PutMapping("/{id}")
    public Mono<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        return movieService.updateMovie(id, movie);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteMovie(@PathVariable Long id) {
        Mono<Movie> movieMono = movieService.getMovieById(id);

        return movieService.deleteMovie(id);
    }
}
