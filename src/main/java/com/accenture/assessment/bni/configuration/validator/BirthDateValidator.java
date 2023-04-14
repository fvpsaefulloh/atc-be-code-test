package com.accenture.assessment.bni.configuration.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class BirthDateValidator implements ConstraintValidator<BirthDateValidation, LocalDate> {

    @Override
    public void initialize(BirthDateValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
    @Override
    public boolean isValid(LocalDate dob, ConstraintValidatorContext constraintValidatorContext) {
        LocalDate yearMinus100 = LocalDate.now().minusYears(100);
        return dob.isAfter(yearMinus100);
    }
}
