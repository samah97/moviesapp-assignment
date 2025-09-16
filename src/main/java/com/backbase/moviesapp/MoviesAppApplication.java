package com.backbase.moviesapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MoviesAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoviesAppApplication.class, args);
    }

}
