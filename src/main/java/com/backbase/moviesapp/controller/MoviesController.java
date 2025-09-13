package com.backbase.moviesapp.controller;

import com.backbase.moviesapp.dtos.response.MovieResponse;
import com.backbase.moviesapp.services.MoviesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/movies")
@RestController
public class MoviesController {

    private final MoviesService moviesService;

    @GetMapping("/winner")
    public ResponseEntity<MovieResponse> winner(@RequestParam("category") String category, @RequestParam("movie") String movieName){
        return ResponseEntity.ok(moviesService.isWinner(category,movieName));
    }

}
