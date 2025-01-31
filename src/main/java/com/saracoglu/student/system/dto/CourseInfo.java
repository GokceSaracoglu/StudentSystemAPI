package com.saracoglu.student.system.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;



public class CourseInfo {
    private Long id;
    @NotEmpty(message = "Course name cannot be empty")
    @Size(min = 2, max = 30)
    private String name;

    public CourseInfo(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CourseInfo() {
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
}

