package com.saracoglu.student.system.dto;

public class CourseRegistrationInfo {
    private Long id;
    private Long studentId; // studentId yerine
    private Long courseId;  // courseId yerine
    private Integer examScore;

    public CourseRegistrationInfo() {
    }

    public CourseRegistrationInfo(Long id, Long studentId, Long courseId, Integer examScore) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
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
