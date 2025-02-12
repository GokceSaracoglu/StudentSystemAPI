package com.saracoglu.student.system.exception.handler;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ApiError {
    private String requestId;
    private Date timestamp;
    private Map<String, List<String>> errors;

    public ApiError(String requestId, Date timestamp, Map<String, List<String>> errors) {
        this.requestId = requestId;
        this.timestamp = timestamp;
        this.errors = errors;
    }

    public String getRequestId() {
        return requestId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }
}
