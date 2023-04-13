package com.accenture.assessment.bni.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 16, unique = true)
    private String ssn;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "middle_name", length = 100)
    private String middleName;

    @Column(name = "family_name", nullable = false, length = 100)
    private String familyName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "created_time", nullable = false)
    private ZonedDateTime createdTime;

    @Column(name = "updated_time")
    private ZonedDateTime updatedTime;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy = "SYSTEM";

    @Column(name = "updated_by", nullable = false, length = 100)
    private String updatedBy = "SYSTEM";

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "deleted_time")
    private ZonedDateTime deletedTime;
}
