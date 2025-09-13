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


    public Mono<OmdbGetMovieResponse> fetchByTitle(String movieTitle) {
        return omdbWebClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .queryParam(TITLE_PARAM, movieTitle)
                                .build()
                )
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new APIException("Error calling OMDB API " + body)))
                )
                .bodyToMono(OmdbGetMovieResponse.class)
                .flatMap(this::handleResponse);
    }

    public Mono<OmdbGetMovieResponse> fetchById(String imdbId) {
        return omdbWebClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .queryParam(IMDB_ID_PARAM, imdbId)
                                .build()
                )
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new APIException("Error calling OMDB API " + body)))
                )
                .bodyToMono(OmdbGetMovieResponse.class)
                .flatMap(this::handleResponse);
    }

    private Mono<OmdbGetMovieResponse> handleResponse(OmdbGetMovieResponse response) {
        if (FALSE_RESPONSE.equalsIgnoreCase(response.Response())) {
            log.error("Error calling OMDB API {}", response.Error());
            if (MOVIE_NOT_FOUND_RESP.equalsIgnoreCase(response.Error()) ||
                    INCORRECT_ID_RESP.equalsIgnoreCase(response.Error())
            ) {
                return Mono.error(new MovieNotFoundException("Movie not found!"));
            }
            return Mono.error(new APIException("API Error:" + response.Error()));
        }
        return Mono.just(response);
    }


}
