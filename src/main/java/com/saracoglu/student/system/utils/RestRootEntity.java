package com.saracoglu.student.system.utils;

import org.springframework.http.HttpStatus;

public class RestRootEntity<T> {

    private Integer status;

    private T payload;

    private String errorMessage;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static <T> RestRootEntity<T> ok(T payload) {
        RestRootEntity<T> rootEntity = new RestRootEntity<>();
        rootEntity.setStatus(HttpStatus.OK.value());
        rootEntity.setErrorMessage(null);
        rootEntity.setPayload(payload);

        return rootEntity;
    }

}