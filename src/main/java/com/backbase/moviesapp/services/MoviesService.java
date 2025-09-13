package com.backbase.moviesapp.services;

import com.backbase.moviesapp.domain.AcademyAward;
import com.backbase.moviesapp.dtos.TopRatedMovies;
import com.backbase.moviesapp.dtos.request.RateMovieRequest;
import com.backbase.moviesapp.dtos.response.MovieRatingResponse;
import com.backbase.moviesapp.dtos.response.MovieResponse;
import com.backbase.moviesapp.utils.MovieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class MoviesService {

    private final AcademyAwardService academyAwardService;
    private final OmdbService omdbService;
    private final RatingService ratingService;

    public MovieResponse isWinner(String category, String movieName) {
        return omdbService.fetchByTitle(movieName)
                .map(omdbResponse -> {
                    List<AcademyAward> academyAwardList = academyAwardService.findWinnersByCategoryAndNominee(category, omdbResponse.Title());
                    boolean hasWon = !academyAwardList.isEmpty();
                    List<String> winnerYears = hasWon ?
                            academyAwardList.stream().map(AcademyAward::getYear).toList()
                            : List.of();
                    return new MovieResponse(omdbResponse.imdbID(), omdbResponse.Title(), omdbResponse.Year(), hasWon, winnerYears);
                }).block();
    }

    public void rateMovie(RateMovieRequest request) {
        omdbService.fetchById(request.imdbId()).blockOptional()
                .ifPresent(imdbResponse ->
                        ratingService.rateMovie(request.imdbId(), request.score())
                );
    }

    public List<MovieRatingResponse> topRated() {
        log.info("Getting Top Rated movies");
        List<TopRatedMovies> topRatedMovies = ratingService.getTopRatedMovies();
        log.info("Top Rated movie = " + topRatedMovies.size());
        return topRatedMovies.stream()
                .map(tpm ->
                        omdbService.fetchById(tpm.imdbId())
                                .map(omdbResponse ->
                                        new MovieRatingResponse(
                                                omdbResponse.imdbID(),
                                                omdbResponse.Title(),
                                                omdbResponse.Year(),
                                                BigDecimal.valueOf(tpm.avgScore())
                                                        .setScale(1, RoundingMode.HALF_UP),
                                                omdbResponse.BoxOffice()
                                        ))
                                .block()
                )
                .sorted(Comparator.comparing((MovieRatingResponse movieRatingResponse) -> movieRatingResponse != null ? MovieUtils.parseBoxOffice(movieRatingResponse.boxOfficeValue()) : BigDecimal.ZERO).reversed())
                .toList();
    }
}
