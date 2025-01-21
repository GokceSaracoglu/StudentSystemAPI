package com.saracoglu.student.system.dto;

import lombok.*;



public class DepartmentInfo {
    private Long id;

    public DepartmentInfo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public DepartmentInfo(String name) {
        this.name = name;
    }
}