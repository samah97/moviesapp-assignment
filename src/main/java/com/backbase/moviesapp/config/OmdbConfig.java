package com.backbase.moviesapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConfigurationProperties(prefix = "omdb")
public class OmdbConfig {

    public String apiKey;

    @Bean
    public WebClient omdbWebClient(WebClient webClient) {
        return webClient;
    }

}
