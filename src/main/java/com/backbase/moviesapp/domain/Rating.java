package com.backbase.moviesapp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "rating")
@NoArgsConstructor
public class Rating {

    public Rating(String imdbId, BigDecimal score) {
        this.imdbId = imdbId;
        this.score = score;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imdbId;

    @Column(precision = 3, scale = 1)
    private BigDecimal score;

    @Column
    private OffsetDateTime datetime;


    @PrePersist
    public void prePersist() {
        this.datetime = OffsetDateTime.now();
    }
}
