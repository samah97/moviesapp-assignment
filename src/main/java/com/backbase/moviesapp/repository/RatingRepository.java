package com.backbase.moviesapp.repository;

import com.backbase.moviesapp.domain.Rating;
import com.backbase.moviesapp.dtos.TopRatedMovies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, String> {

    @Query("""
             SELECT r.imdbId, AVG(r.score) as avgScore, COUNT(r) as count
                       FROM Rating r
                       GROUP BY r.imdbId
            """)
    List<TopRatedMovies> findTopRatedMovies();

}
