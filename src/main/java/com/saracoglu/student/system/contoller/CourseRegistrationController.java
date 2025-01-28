package com.saracoglu.student.system.contoller;

import com.saracoglu.student.system.dto.CourseRegistrationInfo;
import com.saracoglu.student.system.entity.StudentEnrollmentEntity;
import com.saracoglu.student.system.service.CourseCatalogService;
import com.saracoglu.student.system.service.CourseRegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-registrations")
public class CourseRegistrationController {

    @Autowired
    private CourseRegistrationService courseRegistrationService;

    @Autowired
    CourseCatalogService courseCatalogService;

    // Register a student to a course

    @PostMapping
    public ResponseEntity<CourseRegistrationInfo> registerStudentToCourse(@Valid @RequestBody CourseRegistrationInfo registrationInfo) {
        CourseRegistrationInfo registeredCourse = courseRegistrationService.registerStudentToCourse(registrationInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredCourse);
    }

    // Get all course registrations
    @GetMapping
    public List<CourseRegistrationInfo> getAllCourseRegistrations() {
        return courseRegistrationService.getAllCourseRegistrations();
    }

    // Get course registration by ID
    @GetMapping("/{id}")
    public CourseRegistrationInfo getCourseRegistrationById(@PathVariable Long id) {
        return courseRegistrationService.getCourseRegistrationById(id);
    }

    // Delete course registration by ID
    @DeleteMapping("/{id}")
    public void deleteCourseRegistrationById(@PathVariable Long id) {
        courseRegistrationService.deleteCourseRegistrationById(id);
    }
}
