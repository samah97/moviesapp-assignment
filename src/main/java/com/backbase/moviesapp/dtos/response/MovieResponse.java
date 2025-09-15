package com.backbase.moviesapp.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record MovieResponse(
        String imdbId,
        String title,
        String year,
        Boolean hasWon,
        List<String> winnerYears
) {
}
