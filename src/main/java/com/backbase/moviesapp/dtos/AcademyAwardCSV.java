package com.backbase.moviesapp.dtos;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AcademyAwardCSV {

    @CsvBindByName(column = "Year")
    private String year;
    @CsvBindByName(column = "Category")
    private String category;
    @CsvBindByName(column = "Nominee")
    private String nominee;
    @CsvBindByName(column = "Additional Info")
    private String additionalInfo;
    @CsvBindByName(column = "Won?")
    private String hasWon;
}
