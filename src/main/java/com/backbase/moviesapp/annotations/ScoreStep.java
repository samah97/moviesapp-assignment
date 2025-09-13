package com.backbase.moviesapp.annotations;

import com.backbase.moviesapp.validations.ScoreStepValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ScoreStepValidator.class)
public @interface ScoreStep {
    String message() default "must be in increments of 0.5";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
