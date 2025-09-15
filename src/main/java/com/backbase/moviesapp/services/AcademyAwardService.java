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

@Slf4j
@RequiredArgsConstructor
@Service
public class AcademyAwardService {

    private final AcademyAwardsRepository academyAwardsRepository;
    @PersistenceContext
    private final EntityManager entityManager;
    private static final int BATCH_SIZE = 500;


    public AcademyAward save(AcademyAward academyAward) {
        return academyAwardsRepository.save(academyAward);
    }

    @Transactional
    public void clearAndSave(List<AcademyAward> academyAwardList) {
        academyAwardsRepository.deleteAll();
        saveAllInBatch(academyAwardList);
    }


    public void saveAllInBatch(List<AcademyAward> awards) {
        log.info("Starting batch insert of {} awards", awards.size());

        for (int i = 0; i < awards.size(); i++) {
            entityManager.persist(awards.get(i));

            if (i > 0 && i % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        entityManager.flush();
        entityManager.clear();

        log.info("Batch insert completed successfully");
    }

    List<AcademyAward> findWinnersByCategoryAndNominee(String category, String movieName) {
        return academyAwardsRepository.findAllByCategoryAndNomineeAndHasWonTrue(category, movieName);
    }

    public long countAwards() {
        return academyAwardsRepository.count();
    }

}
