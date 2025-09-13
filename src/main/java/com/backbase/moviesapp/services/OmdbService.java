package com.backbase.moviesapp.services;

import com.backbase.moviesapp.dtos.response.omdb.OmdbGetMovieResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class OmdbService {

    private final WebClient omdbWebClient;

    public Mono<OmdbGetMovieResponse> fetchByTitle(){
        String apiKey  = "qweqew";
        return omdbWebClient.get()
                .uri(uriBuilder ->
                        uriBuilder.queryParam("apiKey", apiKey)
                                .queryParam("t","qwe")
                                .build()
                )
                .retrieve()
                .bodyToMono(OmdbGetMovieResponse.class);

    };

}
