package com.saracoglu.student.system.service;

import com.saracoglu.student.system.dto.CourseRegistrationInfo;
import com.saracoglu.student.system.entity.CourseCatalogEntity;
import com.saracoglu.student.system.entity.CourseRegistrationEntity;
import com.saracoglu.student.system.entity.StudentEnrollmentEntity;
import com.saracoglu.student.system.exception.CourseNotFoundException;
import com.saracoglu.student.system.exception.StudentNotFoundException;
import com.saracoglu.student.system.repository.CourseCatalogRepository;
import com.saracoglu.student.system.repository.CourseRegistrationRepository;
import com.saracoglu.student.system.repository.StudentManagementRepository;
import com.saracoglu.student.system.service.mapper.StudentSystemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseRegistrationService implements Serializable {
    private static final long serialVersionUID = 2024013102L;

    @Autowired
    private CourseRegistrationRepository courseRegistrationRepository;

    @Autowired
    private StudentManagementRepository studentManagementRepository;

    @Autowired
    private CourseCatalogRepository courseCatalogRepository;

    @Autowired
    private StudentSystemMapper studentSystemMapper;

    public CourseRegistrationInfo registerStudentToCourse(CourseRegistrationInfo registrationInfo) {
        Long studentId = registrationInfo.getStudentId();
        Long courseId = registrationInfo.getCourseId();

        StudentEnrollmentEntity student = studentManagementRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(String.format("Öğrenci (%s) bulunamadı.", studentId)));
        CourseCatalogEntity course = courseCatalogRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(String.format("Ders (%s) bulunamadı.", courseId)));
        CourseRegistrationEntity registrationEntity = new CourseRegistrationEntity();
        registrationEntity.setStudent(student);
        registrationEntity.setCourse(course);
        if (registrationInfo.getExamScore() != null) {
            registrationEntity.setExamScore(registrationInfo.getExamScore());
        }
        courseRegistrationRepository.save(registrationEntity);
        return studentSystemMapper.convertToDto(registrationEntity);
    }

    public List<CourseRegistrationInfo> getAllCourseRegistrations() {
        List<CourseRegistrationEntity> registrations = courseRegistrationRepository.findAll();
        return registrations.stream()
                .map(studentSystemMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public CourseRegistrationInfo getCourseRegistrationById(Long id) {
        CourseRegistrationEntity registrationEntity = courseRegistrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
        return studentSystemMapper.convertToDto(registrationEntity);
    }

    public void deleteCourseRegistrationById(Long id) {
        if (!courseRegistrationRepository.existsById(id)) {
            throw new RuntimeException("Registration not found");
        }
        courseRegistrationRepository.deleteById(id);
    }
}
