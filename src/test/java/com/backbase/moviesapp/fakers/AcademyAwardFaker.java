package com.backbase.moviesapp.fakers;

import com.backbase.moviesapp.domain.AcademyAward;
import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AcademyAwardFaker {

    private static final Faker faker = new Faker();

    public AcademyAward createAward(String category, String title, boolean hasWon) {
        AcademyAward academyAward = new AcademyAward();
        academyAward.setYear(String.valueOf(faker.number().numberBetween(1900, 2025)));
        academyAward.setCategory(category);
        academyAward.setNominee(title);
        academyAward.setHasWon(hasWon);
        return academyAward;
    }

    public AcademyAward createAward(String title, boolean hasWon) {
        return createAward(faker.book().genre(), title, hasWon);
    }

}
