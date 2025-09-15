package com.backbase.moviesapp.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OMDBConstants {

    public static final String API_KEY_PARAM = "apikey";
    public static final String TITLE_PARAM = "t";
    public static final String IMDB_ID_PARAM = "i";
    public static final String FALSE_RESPONSE = "False";
    public static final String MOVIE_NOT_FOUND_RESP = "Movie not found!";
    public static final String INCORRECT_ID_RESP = "Incorrect IMDb ID.";
}
