package com.saracoglu.student.system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "student")
public class StudentEnrollmentEntity {
    public StudentEnrollmentEntity() {
    }

    public StudentEnrollmentEntity(String firstName, String middleName, String lastName, DepartmentCatalogEntity department) {

        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.department = department;
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

    public DepartmentCatalogEntity getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentCatalogEntity department) {
        this.department = department;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "last_name")
    private String lastName;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentCatalogEntity department;
}
