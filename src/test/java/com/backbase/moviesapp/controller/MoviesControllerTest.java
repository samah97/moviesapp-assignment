package com.backbase.moviesapp.controller;

import com.backbase.moviesapp.dtos.request.RateMovieRequest;
import com.backbase.moviesapp.dtos.response.MovieRatingResponse;
import com.backbase.moviesapp.dtos.response.MovieResponse;
import com.backbase.moviesapp.exceptions.MovieNotFoundException;
import com.backbase.moviesapp.fakers.MovieRatingResponseFaker;
import com.backbase.moviesapp.fakers.MovieResponseFaker;
import com.backbase.moviesapp.services.MoviesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MoviesController.class)
class MoviesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MoviesService moviesService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Test Controller Movie Won Movie")
    @Test
    void test_winner_hasWon() throws Exception {
        String category = "Best Picture";
        String movieName = "Fox";
        boolean hasWon = true;

        MovieResponse movieResponse = MovieResponseFaker.createMovieResponse(movieName, hasWon);
        when(moviesService.isWinner(category, movieName)).thenReturn(movieResponse);

        mockMvc.perform(
                        get("/movies/winner")
                                .param("category", category)
                                .param("movie", movieName)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(movieName))
                .andExpect(jsonPath("$.hasWon").value(hasWon))
                .andExpect(jsonPath("$.winnerYears").isNotEmpty());
    }

    @DisplayName("Test Controller Movie Not Found")
    @Test
    void test_winner_MovieNotFound() throws Exception {
        String category = "Best Picture";
        String movieName = "Invalid Movie";

        when(moviesService.isWinner(category, movieName)).thenThrow(MovieNotFoundException.class);

        mockMvc.perform(
                        get("/movies/winner")
                                .param("category", category)
                                .param("movie", movieName)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void rateMovie_shouldReturnAccepted() throws Exception {
        RateMovieRequest request = new RateMovieRequest("Inception", new BigDecimal(5));

        mockMvc.perform(post("/movies/rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());

        verify(moviesService).rateMovie(any(RateMovieRequest.class));
    }

    @Test
    void topRated() throws Exception {
        List<MovieRatingResponse> movieRatingResponses = List.of(
                MovieRatingResponseFaker.createMovieRatingResponse("Inception"),
                MovieRatingResponseFaker.createMovieRatingResponse("Fox")
        );

        when(moviesService.topRated()).thenReturn(movieRatingResponses);

        mockMvc.perform(get("/movies/top-rated"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(movieRatingResponses.size()))
                .andExpect(jsonPath("$[0].title").value(movieRatingResponses.get(0).title()))
                .andExpect(jsonPath("$[1].title").value(movieRatingResponses.get(1).title()));

    }
}