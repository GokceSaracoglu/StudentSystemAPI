package com.saracoglu.student.system.dto;

import com.saracoglu.student.system.entity.DepartmentCatalogEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class StudentInfo {
    private Long id;
    @NotEmpty(message = "First name cannot to be empty")
    private String firstName;
    private String middleName;
    @NotEmpty(message = "Last name cannot to be empty")
    private String lastName;
    @NotNull(message = "Department ID is required")
    private Long departmentId;

    public StudentInfo(String firstName, String middleName, String lastName, Long departmentId) {
                this.firstName = firstName;
                this.middleName = middleName;
                this.lastName = lastName;
                this.departmentId = departmentId;
    }

    public StudentInfo() {

    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
                this.id = id;
    }



    public String getFirstName() {
                return firstName;
    }

    public void setFirstName(String firstName) {
                this.firstName = firstName;
    }

    public String getMiddleName() {
                return middleName;
    }

    public void setMiddleName(String middleName) {
                this.middleName = middleName;
    }

    public String getLastName() {
                return lastName;
    }

    public void setLastName(String lastName) {
                this.lastName = lastName;
    }

    public Long getDepartmentId() {
                return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
                this.departmentId = departmentId;
    }


}


