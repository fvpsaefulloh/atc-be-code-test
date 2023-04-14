package com.accenture.assessment.bni.configuration;

public class InvalidValueException extends RuntimeException {
    public InvalidValueException(String field, String value) {
        super(String.format("Invalid value for field %s, rejected value : %s ", field, value));
    }
}
