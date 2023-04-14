package com.accenture.assessment.bni.controller.request;

import com.accenture.assessment.bni.configuration.validator.BirthDateValidation;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserRequest {
    @NotEmpty
    @Size(max = 16)
    @Pattern(regexp = "^[0-9]+$", message = "Must be number")
    private String ssn;

    @Size(min = 3, max = 100)
    @JsonProperty("first_name")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Can't contain special character")
    private String firstName;

    @Size(min = 3, max = 100)
    @JsonProperty("last_name")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Can't contain special character")
    private String lastName;

    @NotNull
    @JsonProperty("birth_date")
    @BirthDateValidation()
    private LocalDate birthDate;
}
