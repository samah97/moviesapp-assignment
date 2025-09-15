package com.backbase.moviesapp.fakers;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonFaker {
    private final Faker faker = new Faker();

    public String imdbId() {
        return "tt" + faker.number().digits(7);
    }

    public String boxOfficeValue() {
        return "$" + faker.number().numberBetween(1_000_000, 999_000_000);
    }

}
