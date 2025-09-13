package com.backbase.moviesapp.dtos.request;

import com.backbase.moviesapp.annotations.ScoreStep;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record RateMovieRequest(
        @NotNull
        @Size(min = 1, max = 10)
        String imdbId,

        @NotNull
        @DecimalMin("1.0")
        @DecimalMax("10.0")
        @ScoreStep
        BigDecimal score
) {
}
