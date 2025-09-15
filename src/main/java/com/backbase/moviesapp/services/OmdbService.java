package com.backbase.moviesapp.services;

import com.backbase.moviesapp.dtos.response.omdb.OmdbGetMovieResponse;
import com.backbase.moviesapp.exceptions.APIException;
import com.backbase.moviesapp.exceptions.MovieNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.backbase.moviesapp.constants.OMDBConstants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class OmdbService {

    private final WebClient omdbWebClient;

    private OmdbGetMovieResponse fetchMovie(String queryParamName, String queryParamValue) {
        var response = omdbWebClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .queryParam(queryParamName, queryParamValue)
                                .build()
                )
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, errorResponse ->
                        errorResponse.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new APIException("Error calling OMDB API " + body)))
                )
                .bodyToMono(OmdbGetMovieResponse.class)
                .block();

        return handleResponse(response);
    }

    public OmdbGetMovieResponse fetchByTitle(String movieTitle) {
        return fetchMovie(TITLE_PARAM, movieTitle);
    }

    public OmdbGetMovieResponse fetchById(String imdbId) {
        return fetchMovie(IMDB_ID_PARAM, imdbId);
    }

    private OmdbGetMovieResponse handleResponse(OmdbGetMovieResponse response) {
        if (response == null) {
            throw new APIException("OMDb returned null response");
        }
        if (FALSE_RESPONSE.equalsIgnoreCase(response.Response())) {
            log.error("OMDB API Error {}", response.Error());
            if (MOVIE_NOT_FOUND_RESP.equalsIgnoreCase(response.Error()) ||
                    INCORRECT_ID_RESP.equalsIgnoreCase(response.Error())
            ) {
                throw new MovieNotFoundException("Movie not found!");
            }
            throw new APIException("API Error:" + response.Error());
        }
        return response;
    }
}
