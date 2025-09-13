package com.backbase.moviesapp.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    String title;
    String message;
    List<String> errors;

    public ErrorResponse(String title, String message) {
        this.title = title;
        this.message = message;
    }
}
