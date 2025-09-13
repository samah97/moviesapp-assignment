package com.backbase.moviesapp.services;

import com.backbase.moviesapp.domain.AcademyAward;
import com.backbase.moviesapp.repository.AcademyAwardsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AcademyAwardService {

    private final AcademyAwardsRepository academyAwardsRepository;
    @PersistenceContext
    private final EntityManager entityManager;
    private static final int BATCH_SIZE = 500;

    private void saveAwards(List<AcademyAward> academyAwardList) {
        academyAwardsRepository.saveAll(academyAwardList);
    }

    @Transactional
    public void clearAndSave(List<AcademyAward> academyAwardList){
        academyAwardsRepository.deleteAll();
        saveAllInBatch(academyAwardList);
    }

    @Transactional
    public void saveAllInBatch(List<AcademyAward> awards) {
        log.info("Starting batch insert of {} awards", awards.size());

        for (int i = 0; i < awards.size(); i++) {
            entityManager.persist(awards.get(i));

            // Flush and clear every BATCH_SIZE to avoid memory overhead
            if (i > 0 && i % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        // Final flush and clear for remaining entities
        entityManager.flush();
        entityManager.clear();

        log.info("Batch insert completed successfully");
    }

    List<AcademyAward> findWinnersByCategoryAndNominee(String category, String movieName){
        return academyAwardsRepository.findAllByCategoryAndNomineeAndHasWonTrue(category, movieName);
    }

}
