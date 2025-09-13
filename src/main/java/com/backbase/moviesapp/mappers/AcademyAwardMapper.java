package com.backbase.moviesapp.mappers;

import com.backbase.moviesapp.domain.AcademyAward;
import com.backbase.moviesapp.dtos.AcademyAwardCSV;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AcademyAwardMapper {


    @Mapping(target = "hasWon", source = "hasWon", qualifiedByName = "hasWonMapper")
    AcademyAward mapCSVToDomain(AcademyAwardCSV academyAwardCSV);


    @Named("hasWonMapper")
    default Boolean hasWonMapper(String hasWon) {
        return "YES".equalsIgnoreCase(hasWon);
    }
}
