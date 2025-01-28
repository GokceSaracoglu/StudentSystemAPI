package com.saracoglu.student.system.contoller;

import com.saracoglu.student.system.dto.CourseInfo;
import com.saracoglu.student.system.entity.StudentEnrollmentEntity;
import com.saracoglu.student.system.service.CourseCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseCatalogController {

    @Autowired
    private CourseCatalogService courseCatalogService;

    // Yeni bir ders ekleme
    @PostMapping
    public CourseInfo addCourse(@RequestBody CourseInfo courseInfo) {
        return courseCatalogService.addCourse(courseInfo);
    }

    // ID'ye göre ders getirme
    @GetMapping("/{id}")
    public CourseInfo getCourseById(@PathVariable Long id) {
        return courseCatalogService.getCourseById(id);
    }

    // Tüm dersleri listeleme
    @GetMapping
    public List<CourseInfo> getAllCourses() {
        return courseCatalogService.getCourses();
    }

    // ID'ye göre ders silme
    @DeleteMapping("/{id}")
    public void deleteCourseById(@PathVariable Long id) {
        courseCatalogService.deleteCourseById(id);
    }
    @GetMapping("/{courseId}/students")
    public List<StudentEnrollmentEntity> getStudentsByCourse(@PathVariable Long courseId) {
        return courseCatalogService.getStudentsByCourse(courseId);
    }
}
