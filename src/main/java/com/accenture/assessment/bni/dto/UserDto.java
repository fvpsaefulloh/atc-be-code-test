package com.accenture.assessment.bni.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long id;
    private String ssn;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String createdTime;
    private String updatedTime;
    private String createdBy;
    private String updatedBy;
    private Boolean isActive;
}
