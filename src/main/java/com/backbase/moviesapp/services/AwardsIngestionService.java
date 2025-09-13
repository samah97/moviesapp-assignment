package com.backbase.moviesapp.services;

import com.backbase.moviesapp.domain.AcademyAward;
import com.backbase.moviesapp.dtos.AcademyAwardCSV;
import com.backbase.moviesapp.helpers.csv.CSVMapper;
import com.backbase.moviesapp.mappers.AcademyAwardMapper;
import com.backbase.moviesapp.utils.ChecksumUtility;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class AwardsIngestionService implements ApplicationRunner {

    @Value("${award-winnings-file}")
    public String fileName;

    private String checksumFileName;

    private final ResourceLoader resourceLoader;
    private final AcademyAwardMapper academyAwardMapper;
    private final AcademyAwardService academyAwardService;

    @PostConstruct
    private void init() {
        checksumFileName = fileName + ".checksum";
    }


    @Override
    public void run(ApplicationArguments args){
        try {
            loadCSVData();
        } catch (IOException e) {
            log.error("There was an error loading the CSV Data {}", e.getMessage());
        }
    }

    private void loadCSVData() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:"+fileName);
        String newChecksum;
        try (InputStream is = resource.getInputStream()) {
            newChecksum = ChecksumUtility.computeChecksum(is);
        }
        if(fileIsNew(newChecksum)){
            List<AcademyAwardCSV> academyAwardCSV =  CSVMapper.csvToModelMapper(resource.getInputStream(), AcademyAwardCSV.class);

            List<AcademyAward> academyAwardList = academyAwardCSV.stream()
                    .map(academyAwardMapper::mapCSVToDomain)
                    .toList();
            academyAwardService.clearAndSave(academyAwardList);
            updateNewChecksum(newChecksum);
            log.info("CSV Data saved successfully");
        }
    }

    private void updateNewChecksum(String checkSum) {
        try {
            Path path = Paths.get("src/main/resources/" + checksumFileName);
            Files.writeString(path, checkSum, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            log.info("Checksum updated: {}", checkSum);
        } catch (IOException e) {
            log.error("Failed to update checksum file: {}", e.getMessage(), e);
        }
    }

    private boolean fileIsNew(String newCheckSum) {
        String latestChecksum = getLatestChecksum();
        return ! newCheckSum.equals(latestChecksum);
    }

    private String getLatestChecksum(){
        Resource resource = resourceLoader.getResource("classpath:"+checksumFileName);
        try {
            return  resource.exists()? resource.getContentAsString(Charset.defaultCharset()): Strings.EMPTY;
        } catch (IOException e) {
            log.warn("Failed to read checksum file, assuming new file. {}", e.getMessage());
            return  Strings.EMPTY;
        }
    }
}
