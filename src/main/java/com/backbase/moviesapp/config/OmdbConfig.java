package com.backbase.moviesapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Configuration
public class OmdbConfig {

    @Value("${omdb.api.key}")
    public String apiKey;

    @Value("${omdb.api.base-url:https://www.omdbapi.com/}")
    public String baseUrl;
    private static final String API_KEY_PARAM = "apikey";


    @Bean
    public WebClient omdbWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(baseUrl)
                .filter((request, next) -> {
                    URI uriWithAPIKey = UriComponentsBuilder
                            .fromUri(request.url())
                            .queryParam(API_KEY_PARAM, apiKey)
                            .build(true)
                            .toUri();
                    var clientRequest = ClientRequest.from(request)
                            .url(uriWithAPIKey)
                            .build();

                    return next.exchange(clientRequest);
                })
                .build();
    }

}
