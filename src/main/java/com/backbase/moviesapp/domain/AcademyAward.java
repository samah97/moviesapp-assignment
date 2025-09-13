package com.backbase.moviesapp.domain;

import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "academy_awards")
public class AcademyAward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String year;
    private String category;
    private String nominee;
    private String additionalInfo;
    private Boolean hasWon;
}
