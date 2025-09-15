package com.backbase.moviesapp.fakers;

import com.backbase.moviesapp.dtos.TopRatedMovies;
import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TopRatedMoviesFaker {
    private static final Faker faker = new Faker();

    public TopRatedMovies createTopRatedMovies(String imdbId) {
        return new TopRatedMovies(
                imdbId,
                faker.number().randomDouble(1, 1, 10),
                faker.number().randomNumber()
        );
    }


}
