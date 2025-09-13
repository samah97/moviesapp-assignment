package com.backbase.moviesapp.dtos;

public record TopRatedMovies(
        String imdbId,
        Double avgScore,
        Long count
) {
}
