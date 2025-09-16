package com.backbase.moviesapp.services;

import com.backbase.moviesapp.domain.AcademyAward;
import com.backbase.moviesapp.dtos.TopRatedMovies;
import com.backbase.moviesapp.dtos.request.RateMovieRequest;
import com.backbase.moviesapp.dtos.response.MovieRatingResponse;
import com.backbase.moviesapp.dtos.response.MovieResponse;
import com.backbase.moviesapp.dtos.response.omdb.OmdbGetMovieResponse;
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
    private static final String DEFAULT_CATEGORY = "Best Picture";

    public MovieResponse isWinner(String category, String movieName) {
        if (category == null || category.isEmpty()) {
            category = DEFAULT_CATEGORY;
        }
        OmdbGetMovieResponse omdb = omdbService.fetchByTitle(movieName);
        List<AcademyAward> academyAwardList = academyAwardService.findWinnersByCategoryAndNominee(category, omdb.Title());

        boolean hasWon = !academyAwardList.isEmpty();
        List<String> winnerYears = academyAwardList.stream()
                .map(AcademyAward::getYear)
                .toList();

        return new MovieResponse(
                omdb.imdbID(),
                omdb.Title(),
                omdb.Year(),
                hasWon,
                winnerYears
        );
    }

    public void rateMovie(RateMovieRequest request) {
        omdbService.fetchById(request.imdbId()); // If movie is not found, exception will be thrown
        ratingService.rateMovie(request.imdbId(), request.score());
    }

    public List<MovieRatingResponse> topRated() {
        List<TopRatedMovies> topRatedMovies = ratingService.getTopRatedMovies();

        return topRatedMovies.stream()
                .map(tpm -> {
                    var omdb = omdbService.fetchById(tpm.imdbId());
                    return new MovieRatingResponse(
                            omdb.imdbID(),
                            omdb.Title(),
                            omdb.Year(),
                            BigDecimal.valueOf(tpm.avgScore())
                                    .setScale(1, RoundingMode.HALF_UP),
                            omdb.BoxOffice()
                    );
                })
                .sorted(Comparator.comparing((MovieRatingResponse r) -> MovieUtils.parseBoxOffice(r.boxOfficeValue()))
                        .reversed()
                )
                .toList();
    }
}
