package com.saracoglu.student.system.service;

import com.saracoglu.student.system.dto.CourseRegistrationInfo;
import com.saracoglu.student.system.entity.CourseCatalogEntity;
import com.saracoglu.student.system.entity.CourseRegistrationEntity;
import com.saracoglu.student.system.entity.StudentEnrollmentEntity;
import com.saracoglu.student.system.repository.CourseCatalogRepository;
import com.saracoglu.student.system.repository.CourseRegistrationRepository;
import com.saracoglu.student.system.repository.StudentManagementRepository;
import com.saracoglu.student.system.service.mapper.StudentSystemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseRegistrationService {

    @Autowired
    private CourseRegistrationRepository courseRegistrationRepository;

    @Autowired
    private StudentManagementRepository studentManagementRepository;

    @Autowired
    private CourseCatalogRepository courseCatalogRepository;

    @Autowired
    private StudentSystemMapper studentSystemMapper;

    // Register a student to a course
    public CourseRegistrationInfo registerStudentToCourse(CourseRegistrationInfo registrationInfo) {
        // Öğrenci ve ders ID'lerini al
        Long studentId = registrationInfo.getStudentId();
        Long courseId = registrationInfo.getCourseId();

        // Öğrenciyi ve dersi veritabanından bul
        StudentEnrollmentEntity student = studentManagementRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        CourseCatalogEntity course = courseCatalogRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // CourseRegistrationEntity nesnesini oluştur
        CourseRegistrationEntity registrationEntity = new CourseRegistrationEntity();
        registrationEntity.setStudent(student);
        registrationEntity.setCourse(course);

        // examScore opsiyonel, eğer varsa kaydet
        if (registrationInfo.getExamScore() != null) {
            registrationEntity.setExamScore(registrationInfo.getExamScore());
        }

        // Kayıt işlemi
        courseRegistrationRepository.save(registrationEntity);

        // DTO'ya dönüştür ve döndür
        return studentSystemMapper.convertToDto(registrationEntity);
    }

    // Get all course registrations
    public List<CourseRegistrationInfo> getAllCourseRegistrations() {
        List<CourseRegistrationEntity> registrations = courseRegistrationRepository.findAll();
        return registrations.stream()
                .map(studentSystemMapper::convertToDto)
                .collect(Collectors.toList());
    }

    // Get course registration by ID
    public CourseRegistrationInfo getCourseRegistrationById(Long id) {
        CourseRegistrationEntity registrationEntity = courseRegistrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
        return studentSystemMapper.convertToDto(registrationEntity);
    }

    // Delete course registration by ID
    public void deleteCourseRegistrationById(Long id) {
        if (!courseRegistrationRepository.existsById(id)) {
            throw new RuntimeException("Registration not found");
        }
        courseRegistrationRepository.deleteById(id);
    }
}
