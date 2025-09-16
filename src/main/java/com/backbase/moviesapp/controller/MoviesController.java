package com.backbase.moviesapp.controller;

import com.backbase.moviesapp.dtos.request.RateMovieRequest;
import com.backbase.moviesapp.dtos.response.MovieRatingResponse;
import com.backbase.moviesapp.dtos.response.MovieResponse;
import com.backbase.moviesapp.services.MoviesService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/api/movies")
@RestController
public class MoviesController {

    private final MoviesService moviesService;

    @Operation(description = "Checks if a movie has ever won an award in the input category")
    @GetMapping("/winner")
    public ResponseEntity<MovieResponse> winner(@Nullable @RequestParam("category") String category, @NotBlank @RequestParam("movie") String movie) {
        return ResponseEntity.ok(
                moviesService.isWinner(category, movie)
        );
    }

    @Operation(description = "Rates a movie")
    @PostMapping("/rate")
    public ResponseEntity<Void> rateMovie(@Valid @RequestBody RateMovieRequest rateMovieRequest) {
        moviesService.rateMovie(rateMovieRequest);
        return ResponseEntity.accepted().build();
    }

    @Operation(description = "Retrieves a list of Top Rated movies")
    @GetMapping("/top-rated")
    public ResponseEntity<List<MovieRatingResponse>> topRated() {
        return ResponseEntity.ok(moviesService.topRated());
    }
}
