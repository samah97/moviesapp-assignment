package com.backbase.moviesapp.fakers;

import com.backbase.moviesapp.dtos.response.MovieResponse;
import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class MovieResponseFaker {

    private static final Faker faker = new Faker();

    public MovieResponse createMovieResponse(String title, boolean hasWon) {
        return new MovieResponse(
                CommonFaker.imdbId(),
                title,
                String.valueOf(faker.number().numberBetween(1950, 2025)),
                hasWon,
                hasWon ? List.of(String.valueOf(faker.number().numberBetween(1950, 1999))) : null
        );
    }


}
