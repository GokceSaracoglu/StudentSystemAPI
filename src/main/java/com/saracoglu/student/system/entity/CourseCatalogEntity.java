package com.saracoglu.student.system.entity;

import jakarta.persistence.*;

@Entity

@Table(name = "course")
public class CourseCatalogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;

    public CourseCatalogEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CourseCatalogEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
