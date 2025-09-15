package com.backbase.moviesapp.fakers;

import com.backbase.moviesapp.dtos.response.MovieRatingResponse;
import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public class MovieRatingResponseFaker {

    private static final Faker faker = new Faker();

    public MovieRatingResponse createMovieRatingResponse(String title) {
        return new MovieRatingResponse(
                CommonFaker.imdbId(),
                title,
                String.valueOf(faker.number().numberBetween(1950, 2025)),
                BigDecimal.valueOf(faker.number().randomDouble(1, 1, 10))
                        .setScale(1, RoundingMode.HALF_UP),
                CommonFaker.boxOfficeValue()
        );
    }


}
