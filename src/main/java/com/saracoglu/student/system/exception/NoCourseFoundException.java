package com.saracoglu.student.system.exception;

public class NoCourseFoundException extends RuntimeException{
    public NoCourseFoundException(String message) {
        super(message);
    }
}
