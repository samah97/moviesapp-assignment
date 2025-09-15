package com.backbase.moviesapp.controller;

import com.backbase.moviesapp.dtos.request.RateMovieRequest;
import com.backbase.moviesapp.dtos.response.MovieRatingResponse;
import com.backbase.moviesapp.dtos.response.MovieResponse;
import com.backbase.moviesapp.services.MoviesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/movies")
@RestController
public class MoviesController {

    private final MoviesService moviesService;

    @GetMapping("/winner")
    public ResponseEntity<MovieResponse> winner(@NotBlank @RequestParam("category") String category, @NotBlank @RequestParam("movie") String movie) {
        return ResponseEntity.ok(
                moviesService.isWinner(category, movie)
        );
    }

    @PostMapping("/rate")
    public ResponseEntity<Void> rateMovie(@Valid @RequestBody RateMovieRequest rateMovieRequest) {
        moviesService.rateMovie(rateMovieRequest);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<MovieRatingResponse>> topRated() {
        return ResponseEntity.ok(moviesService.topRated());
    }
}
