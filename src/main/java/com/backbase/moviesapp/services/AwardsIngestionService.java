package com.backbase.moviesapp.services;

import com.backbase.moviesapp.domain.AcademyAward;
import com.backbase.moviesapp.dtos.AcademyAwardCSV;
import com.backbase.moviesapp.helpers.csv.CSVMapper;
import com.backbase.moviesapp.mappers.AcademyAwardMapper;
import com.backbase.moviesapp.utils.ChecksumUtility;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Profile("!test")
@Component
public class AwardsIngestionService implements ApplicationRunner {

    @Value("${award-winnings-file}")
    private String fileName;

    private String checksumFileName;

    private final ResourceLoader resourceLoader;
    private final AcademyAwardMapper academyAwardMapper;
    private final AcademyAwardService academyAwardService;

    @PostConstruct
    private void init() {
        checksumFileName = fileName + ".checksum";
    }


    @Override
    public void run(ApplicationArguments args) {
        try {
            loadCSVData();
        } catch (Exception e) {
            log.error("There was an error loading the CSV Data {}", e.getMessage());
        }
    }

    private void loadCSVData() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:" + fileName);
        if (!resource.exists()) {
            log.warn("CSV file not found: {}", fileName);
            return;
        }
        String newChecksum = computeFileChecksum(resource);
        if (shouldLoadData(newChecksum)) {
            List<AcademyAwardCSV> academyAwardCSV = parseCSVFile(resource);
            List<AcademyAward> academyAwardList = mapToEntities(academyAwardCSV);

            academyAwardService.clearAndSave(academyAwardList);
            updateNewChecksum(newChecksum);

            log.info("CSV Data saved successfully. {} records processed", academyAwardList.size());
        } else {
            log.info("CSV file unchanged, skipping data load");
        }
    }

    private String computeFileChecksum(Resource resource) throws IOException {
        try (InputStream is = resource.getInputStream()) {
            return ChecksumUtility.computeChecksum(is);
        }
    }

    private List<AcademyAwardCSV> parseCSVFile(Resource resource) throws IOException {
        try (InputStream is = resource.getInputStream()) {
            return CSVMapper.csvToModelMapper(is, AcademyAwardCSV.class);
        }
    }


    private boolean shouldLoadData(String newChecksum) {
        return academyAwardService.countAwards() == 0 ||
                fileIsNew(newChecksum);
    }

    private List<AcademyAward> mapToEntities(List<AcademyAwardCSV> csvRecords) {
        return csvRecords.stream()
                .map(academyAwardMapper::mapCSVToDomain)
                .filter(Objects::nonNull) // Filter out any null mappings
                .toList();
    }

    private void updateNewChecksum(String checkSum) {
        try {
            Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
            Path checksumPath = tempDir.resolve(checksumFileName);

            Path parentDir = checksumPath.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }

            Files.writeString(checksumPath, checkSum, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            log.info("Checksum updated: {} at {}", checkSum, checksumPath);
        } catch (IOException e) {
            log.error("Failed to update checksum file", e);
        }
    }

    private boolean fileIsNew(String newCheckSum) {
        String latestChecksum = getLatestChecksum();
        log.info("Oldest checksum: {} ;; New Checksum = {}", latestChecksum, newCheckSum);
        return !newCheckSum.equals(latestChecksum);
    }

    private String getLatestChecksum() {
        try {
            Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
            Path checksumPath = tempDir.resolve(checksumFileName);

            if (Files.exists(checksumPath)) {
                return Files.readString(checksumPath, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.warn("Failed to read checksum file, assuming new file", e);
        }

        return ""; // Use empty string instead of Strings.EMPTY
    }
}
