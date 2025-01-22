package com.saracoglu.student.system.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class CombinedValidationException extends RuntimeException {
    private final List<String> errorMessages;

    public CombinedValidationException(List<String> errorMessages) {
        super("Validation errors occurred");
        this.errorMessages = errorMessages;
    }

}