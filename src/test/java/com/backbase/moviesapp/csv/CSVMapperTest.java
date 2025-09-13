package com.backbase.moviesapp.csv;

import com.backbase.moviesapp.helpers.csv.CSVMapper;
import com.backbase.moviesapp.dtos.AcademyAwardCSV;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CSVMapperTest {

    @Test
    void csvToModelMapper() {
        //Given
        InputStream fileIS = getClass().getResourceAsStream("/sample.csv");
        assertNotNull(fileIS);
        List<AcademyAwardCSV> expectedList = createExpectedSampleList();

        //When
        List<AcademyAwardCSV> actualList = CSVMapper.csvToModelMapper(fileIS, AcademyAwardCSV.class);


        //Then
        assertThat(actualList)
                .hasSize(expectedList.size())
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    private List<AcademyAwardCSV> createExpectedSampleList() {
        return List.of(
                new AcademyAwardCSV("2010 (83rd)","Actor -- Leading Role","Javier Bardem","Biutiful {'Uxbal'}","NO"),
                new AcademyAwardCSV("2010 (83rd)","Actor -- Leading Role","Jeff Bridges","True Grit {'Rooster Cogburn'}","NO"),
                new AcademyAwardCSV("2010 (83rd)","Actor -- Leading Role","Jesse Eisenberg","The Social Network {'Mark Zuckerberg'}","NO")
        );
    }
}