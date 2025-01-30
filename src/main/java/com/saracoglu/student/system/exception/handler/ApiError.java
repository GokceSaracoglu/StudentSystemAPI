package com.saracoglu.student.system.exception.handler;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class ApiError {
    private String id;
    private Date errorTime;
    private Map<String, List<String>> errors;

    public ApiError(String id, Date errorTime, Map<String, List<String>> errors) {
        this.id = id;
        this.errorTime = errorTime;
        this.errors = errors;
    }

    public ApiError() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getErrorTime() {
        return errorTime;
    }

    public void setErrorTime(Date errorTime) {
        this.errorTime = errorTime;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, List<String>> errors) {
        this.errors = errors;
    }
}