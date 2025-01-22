package com.saracoglu.student.system.exception;

public class NoStudentsFoundException extends RuntimeException{
    public NoStudentsFoundException(String message) {
        super(message);
    }
}
