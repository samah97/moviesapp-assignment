package com.backbase.moviesapp.dtos.response.omdb;

import java.util.List;

public record OmdbGetMovieResponse(
        String Title,
        String Year,
        String Rated,
        String Released,
        String Runtime,
        String Genre,
        String Director,
        String Writer,
        String Actors,
        String Plot,
        String Language,
        String Country,
        String Awards,
        String Poster,
        List<Rating> Ratings,
        String Metascore,
        String imdbRating,
        String imdbVotes,
        String imdbID,
        String Type,
        String BoxOffice,
        String totalSeasons,
        String Response,
        String Error
) {
    public record Rating(
            String Source,
            String Value
    ) {
    }


}
