package com.saracoglu.student.system.service.mapper;

import com.saracoglu.student.system.dto.*;
import com.saracoglu.student.system.entity.*;
import org.springframework.stereotype.Component;

@Component
public class StudentSystemMapper {

    // StudentInfo <-> StudentEnrollmentEntity
    public StudentEnrollmentEntity convertToEntity(StudentInfo studentInfo) {
        StudentEnrollmentEntity studentEntity = new StudentEnrollmentEntity();
        studentEntity.setId(studentInfo.getId());
        studentEntity.setFirstName(studentInfo.getFirstName());
        studentEntity.setMiddleName(studentInfo.getMiddleName());
        studentEntity.setLastName(studentInfo.getLastName());
        return studentEntity;
    }

    public StudentInfo convertToDto(StudentEnrollmentEntity studentEntity) {
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setId(studentEntity.getId());
        studentInfo.setFirstName(studentEntity.getFirstName());
        studentInfo.setMiddleName(studentEntity.getMiddleName());
        studentInfo.setLastName(studentEntity.getLastName());
        if (studentEntity.getDepartment() != null) {
            studentInfo.setDepartmentId(studentEntity.getDepartment().getId());
        }
        return studentInfo;
    }

    // CourseInfo <-> CourseCatalogEntity
    public CourseCatalogEntity convertToEntity(CourseInfo courseInfo) {
        CourseCatalogEntity courseEntity = new CourseCatalogEntity();
        courseEntity.setId(courseInfo.getId());
        courseEntity.setName(courseInfo.getName());
        return courseEntity;
    }

    public CourseInfo convertToDto(CourseCatalogEntity courseEntity) {
        CourseInfo courseInfo = new CourseInfo();
        courseInfo.setId(courseEntity.getId());
        courseInfo.setName(courseEntity.getName());
        return courseInfo;
    }

    // CourseRegistrationInfo <-> CourseRegistrationEntity
    public CourseRegistrationEntity convertToEntity(CourseRegistrationInfo courseRegistrationInfo) {
        CourseRegistrationEntity courseRegistrationEntity = new CourseRegistrationEntity();
        courseRegistrationEntity.setId(courseRegistrationInfo.getId());
        courseRegistrationEntity.setExamScore(courseRegistrationInfo.getExamScore());

        // Convert studentId and courseId into student and course entities
        if (courseRegistrationInfo.getStudentId() != null) {
            StudentEnrollmentEntity student = new StudentEnrollmentEntity();
            student.setId(courseRegistrationInfo.getStudentId());
            courseRegistrationEntity.setStudent(student);
        }

        if (courseRegistrationInfo.getCourseId() != null) {
            CourseCatalogEntity course = new CourseCatalogEntity();
            course.setId(courseRegistrationInfo.getCourseId());
            courseRegistrationEntity.setCourse(course);
        }

        return courseRegistrationEntity;
    }

    public CourseRegistrationInfo convertToDto(CourseRegistrationEntity courseRegistrationEntity) {
        CourseRegistrationInfo courseRegistrationInfo = new CourseRegistrationInfo();
        courseRegistrationInfo.setId(courseRegistrationEntity.getId());
        courseRegistrationInfo.setExamScore(courseRegistrationEntity.getExamScore());
        courseRegistrationInfo.setStudentId(courseRegistrationEntity.getStudent() != null ? courseRegistrationEntity.getStudent().getId() : null);
        courseRegistrationInfo.setCourseId(courseRegistrationEntity.getCourse() != null ? courseRegistrationEntity.getCourse().getId() : null);
        return courseRegistrationInfo;
    }

    // DepartmentInfo <-> DepartmentCatalogEntity
    public DepartmentCatalogEntity convertToEntity(DepartmentInfo departmentInfo) {
        DepartmentCatalogEntity departmentEntity = new DepartmentCatalogEntity();
        departmentEntity.setId(departmentInfo.getId());
        departmentEntity.setName(departmentInfo.getName());
        return departmentEntity;
    }

    public DepartmentInfo convertToDto(DepartmentCatalogEntity departmentEntity) {
        DepartmentInfo departmentInfo = new DepartmentInfo();
        departmentInfo.setId(departmentEntity.getId());
        departmentInfo.setName(departmentEntity.getName());
        return departmentInfo;
    }
}
