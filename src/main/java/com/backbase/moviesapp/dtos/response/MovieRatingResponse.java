package com.backbase.moviesapp.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;


@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record MovieRatingResponse(
        String imdbId,
        String movieName,
        String year,
        BigDecimal rating,
        String boxOfficeValue
) {
}
