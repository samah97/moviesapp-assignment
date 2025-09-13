package com.backbase.moviesapp.validations;

import com.backbase.moviesapp.annotations.ScoreStep;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class ScoreStepValidator implements ConstraintValidator<ScoreStep, BigDecimal> {

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) return true;
        return value.remainder(BigDecimal.valueOf(0.5)).compareTo(BigDecimal.ZERO) == 0;
    }
}
