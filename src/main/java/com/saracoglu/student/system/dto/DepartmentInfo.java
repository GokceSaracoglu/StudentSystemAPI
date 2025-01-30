package com.saracoglu.student.system.dto;

import jakarta.validation.constraints.NotEmpty;



public class DepartmentInfo {
    private Long id;
    @NotEmpty(message = "Departmant name cannot to be empty")
    private String name;

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

    public DepartmentInfo(String name) {
        this.name = name;
    }
}