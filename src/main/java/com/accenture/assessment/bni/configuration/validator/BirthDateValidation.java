package com.accenture.assessment.bni.configuration.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target( { ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = BirthDateValidator.class)
public @interface BirthDateValidation {

    public String message() default "Invalid date";
    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default {};
}
