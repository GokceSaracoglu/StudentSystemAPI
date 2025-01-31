package com.saracoglu.student.system.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class CourseRegistrationInfo {
    private Long id;
    private Long studentId; // studentId yerine
    private Long courseId;  // courseId yerine
    @Min(0)  @Max(100)
    private Integer examScore;

    public CourseRegistrationInfo() {
    }

    public CourseRegistrationInfo(Long id, Long courseId, Long studentId, Integer examScore) {
        this.id = id;
        this.courseId = courseId;
        this.studentId = studentId;
        this.examScore = examScore;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Integer getExamScore() {
        return examScore;
    }

    public void setExamScore(Integer examScore) {
        this.examScore = examScore;
    }
}
