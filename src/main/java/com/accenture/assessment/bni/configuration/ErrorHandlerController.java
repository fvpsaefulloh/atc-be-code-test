package com.accenture.assessment.bni.configuration;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class ErrorHandlerController {
    private final static Integer NOT_FOUND_CODE = 30000;
    private final static Integer UNIQUE_VALUE_CODE = 30001;
    private final static Integer INVALID_VALUE_CODE = 30002;
    private final static Integer SYSTEM_ERROR_CODE = 80000;


    @ExceptionHandler(value = {EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse resourceNotFoundException(EntityNotFoundException ex, WebRequest webRequest) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.name(),
                NOT_FOUND_CODE, Collections.singletonList(ex.getMessage()));
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse uniqueDataException(DataIntegrityViolationException ex) {
        return new ErrorResponse(HttpStatus.CONFLICT.name(),
                UNIQUE_VALUE_CODE, Collections.singletonList(ex.getMessage()));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse invalidValueException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<ObjectError> errorList = bindingResult.getAllErrors();

        List<String> errorMessages = new ArrayList<>();
        for (ObjectError objectError : errorList) {
            if (objectError instanceof FieldError) {
                FieldError field = (FieldError) objectError;
                String error = String.format("Invalid value for field %s, rejected value : %s ",
                        field.getField(), field.getRejectedValue());
                errorMessages.add(error);
            }
        }

        return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.name(),
                INVALID_VALUE_CODE, errorMessages);
    }

    @ExceptionHandler(value = {InvalidValueException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse invalidValueException(InvalidValueException ex) {

        return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.name(),
                INVALID_VALUE_CODE, Collections.singletonList(ex.getMessage()));
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse internalServerError(MethodArgumentNotValidException ex) {

        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(),
                SYSTEM_ERROR_CODE, Collections.singletonList("System error, we're unable to process your request at the moment"));
    }
}
