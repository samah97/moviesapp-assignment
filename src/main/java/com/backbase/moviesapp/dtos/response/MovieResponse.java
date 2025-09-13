package com.backbase.moviesapp.dtos.response;

import java.util.List;

public record MovieResponse(
        String movieName,
        Boolean hasWon,
        List<String> winnerYears
) {
}
