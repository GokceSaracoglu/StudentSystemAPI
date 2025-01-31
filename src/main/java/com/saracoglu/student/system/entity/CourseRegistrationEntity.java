package com.saracoglu.student.system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "course_registration")
public class CourseRegistrationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentEnrollmentEntity student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseCatalogEntity course;
    @Column(name = "exam_score")
    private Integer examScore;

    public CourseRegistrationEntity() {
    }

    public CourseRegistrationEntity(Long id, Integer examScore, CourseCatalogEntity course, StudentEnrollmentEntity student) {
        this.id = id;
        this.examScore = examScore;
        this.course = course;
        this.student = student;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentEnrollmentEntity getStudent() {
        return student;
    }

    public void setStudent(StudentEnrollmentEntity student) {
        this.student = student;
    }

    public CourseCatalogEntity getCourse() {
        return course;
    }

    public void setCourse(CourseCatalogEntity course) {
        this.course = course;
    }

    public Integer getExamScore() {
        return examScore;
    }

    public void setExamScore(Integer examScore) {
        this.examScore = examScore;
    }
}
