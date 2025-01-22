package com.saracoglu.student.system.exception;

public class DepartmentNotFoundException  extends RuntimeException{
    public DepartmentNotFoundException(String message) {
        super(message);
    }
}
