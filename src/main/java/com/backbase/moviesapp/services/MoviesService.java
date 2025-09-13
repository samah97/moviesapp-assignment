package com.backbase.moviesapp.services;

import com.backbase.moviesapp.domain.AcademyAward;
import com.backbase.moviesapp.dtos.response.MovieResponse;
import com.backbase.moviesapp.dtos.response.omdb.OmdbGetMovieResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MoviesService {

    private final AcademyAwardService academyAwardService;
    private final OmdbService omdbService;

    public MovieResponse isWinner(String category, String movieName) {
        OmdbGetMovieResponse omdbGetMovieResponse = omdbService.fetchByTitle().block();
        List<AcademyAward> academyAwardList = academyAwardService.findWinnersByCategoryAndNominee(category, movieName);
        if (! academyAwardList.isEmpty()) {
            return new MovieResponse(
                    movieName,
                    true,
                    academyAwardList.stream().map(AcademyAward::getYear).toList()
            );
        }
        throw new NoSuchElementException("Movie not found");
    }

}
