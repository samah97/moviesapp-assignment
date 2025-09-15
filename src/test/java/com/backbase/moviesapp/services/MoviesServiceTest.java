package com.backbase.moviesapp.services;

import com.backbase.moviesapp.domain.AcademyAward;
import com.backbase.moviesapp.dtos.TopRatedMovies;
import com.backbase.moviesapp.dtos.request.RateMovieRequest;
import com.backbase.moviesapp.dtos.response.MovieRatingResponse;
import com.backbase.moviesapp.dtos.response.omdb.OmdbGetMovieResponse;
import com.backbase.moviesapp.exceptions.MovieNotFoundException;
import com.backbase.moviesapp.fakers.AcademyAwardFaker;
import com.backbase.moviesapp.fakers.OmdbGetMovieResponseFaker;
import com.backbase.moviesapp.fakers.TopRatedMoviesFaker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MoviesServiceTest {

    @Mock
    private AcademyAwardService academyAwardService;
    @Mock
    private OmdbService omdbService;
    @Mock
    private RatingService ratingService;
    @InjectMocks
    private MoviesService moviesService;

    @Test
    void isWinner_shouldReturnWinner_whenAwardExists() {
        //Given
        String movieTitle = "Inception";
        String category = "Best Picture";
        OmdbGetMovieResponse omdb = OmdbGetMovieResponseFaker.createResponse(movieTitle);
        AcademyAward academyAward = AcademyAwardFaker.createAward(movieTitle, true);

        when(omdbService.fetchByTitle(movieTitle)).thenReturn(omdb);
        when(academyAwardService.findWinnersByCategoryAndNominee(category, movieTitle)).thenReturn(List.of(academyAward));

        var actual = moviesService.isWinner(category, movieTitle);

        //Then
        assertThat(actual).isNotNull();
        assertThat(actual.title()).isEqualTo(movieTitle);
        assertThat(actual.hasWon()).isTrue();
    }

    @Test
    void isWinner_ShouldThrow_WhenMovieNotFound() {
        String movieTitle = "Unknown";
        when(omdbService.fetchByTitle(movieTitle))
                .thenThrow(new MovieNotFoundException("Movie not found"));

        assertThatThrownBy(() -> moviesService.isWinner("Best Picture", movieTitle))
                .isInstanceOf(MovieNotFoundException.class);
    }

    @Test
    void rateMovie() {
        String movieTitle = "Inception";
        String imdbId = "tt1375666";
        BigDecimal rating = new BigDecimal(5);
        OmdbGetMovieResponse omdb = OmdbGetMovieResponseFaker.createResponse(movieTitle);
        RateMovieRequest request = new RateMovieRequest(imdbId, rating);

        when(omdbService.fetchById(imdbId)).thenReturn(omdb);

        moviesService.rateMovie(request);

        verify(ratingService).rateMovie(imdbId, rating);
    }

    @Test
    void topRated() {
        String inceptionMovieTitle = "Inception";
        String godfatherMovieTitle = "The Godfather";
        String inceptionId = "tt3413132";
        String godfatherId = "tt3413519";
        String inceptionBoxOfficeValue = "$800,000,000";
        String godfatherBoxOfficeValue = "$450,000,000";
        OmdbGetMovieResponse inceptionResponse = OmdbGetMovieResponseFaker.createResponse(inceptionId, inceptionMovieTitle, inceptionBoxOfficeValue);
        OmdbGetMovieResponse godfatherResponse = OmdbGetMovieResponseFaker.createResponse(godfatherId, godfatherMovieTitle, godfatherBoxOfficeValue);

        List<TopRatedMovies> topRatedMoviesList = List.of(
                TopRatedMoviesFaker.createTopRatedMovies(inceptionId),
                TopRatedMoviesFaker.createTopRatedMovies(godfatherId)
        );

        when(ratingService.getTopRatedMovies()).thenReturn(topRatedMoviesList);
        when(omdbService.fetchById(inceptionId)).thenReturn(inceptionResponse);
        when(omdbService.fetchById(godfatherId)).thenReturn(godfatherResponse);

        //When
        List<MovieRatingResponse> result = moviesService.topRated();

        //Then
        assertThat(result).extracting(MovieRatingResponse::title)
                .containsExactly(inceptionMovieTitle, godfatherMovieTitle);
    }
}