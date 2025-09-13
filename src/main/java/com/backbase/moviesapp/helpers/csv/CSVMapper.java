package com.backbase.moviesapp.helpers.csv;

import com.backbase.moviesapp.exceptions.CSVMappingException;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.experimental.UtilityClass;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@UtilityClass
public class CSVMapper {

    public <T> List<T> csvToModelMapper(InputStream file, Class<T> mappingClass ){
        try(Reader reader = new InputStreamReader(file, StandardCharsets.UTF_8)) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader).withType(mappingClass).build();
            return csvToBean.parse();
        }catch(Exception e){
            throw new CSVMappingException(e.getMessage());
        }
    }
}
