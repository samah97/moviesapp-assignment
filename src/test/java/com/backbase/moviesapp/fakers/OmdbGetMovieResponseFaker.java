package com.backbase.moviesapp.fakers;

import com.backbase.moviesapp.dtos.response.omdb.OmdbGetMovieResponse;
import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class OmdbGetMovieResponseFaker {

    private static Faker faker = new Faker();

    public OmdbGetMovieResponse createResponse(String imdbId, String title, String boxOfficeValue) {
        return new OmdbGetMovieResponse(
                title,
                String.valueOf(faker.number().numberBetween(1950, 2025)),
                faker.options().option("G", "PG", "PG-13", "R"), // Rated
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                List.of(new OmdbGetMovieResponse.Rating("Internet", "9/10")),
                null,
                null,
                null,
                imdbId,
                "movie",
                boxOfficeValue,
                null,
                "True",
                null
        );
    }

    public OmdbGetMovieResponse createResponse(String title) {
        return createResponse(
                CommonFaker.imdbId(),
                title,
                CommonFaker.boxOfficeValue()
        );
    }
        

}
