package com.backbase.moviesapp.services;

import com.backbase.moviesapp.domain.Rating;
import com.backbase.moviesapp.dtos.TopRatedMovies;
import com.backbase.moviesapp.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    @Transactional
    public void rateMovie(String movieId, BigDecimal score) {
        ratingRepository.save(
                new Rating(movieId, score)
        );
    }

    public List<TopRatedMovies> getTopRatedMovies() {
        return ratingRepository.findTopRatedMovies();
    }

}
