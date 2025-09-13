package com.backbase.moviesapp.repository;

import com.backbase.moviesapp.domain.AcademyAward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcademyAwardsRepository extends JpaRepository<AcademyAward, Long> {
    List<AcademyAward> findAllByCategoryAndNomineeAndHasWonTrue(String category, String nominee);
}
