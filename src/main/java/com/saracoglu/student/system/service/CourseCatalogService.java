package com.saracoglu.student.system.service;

import com.saracoglu.student.system.dto.CourseInfo;
import com.saracoglu.student.system.entity.CourseCatalogEntity;
import com.saracoglu.student.system.entity.CourseRegistrationEntity;
import com.saracoglu.student.system.entity.StudentEnrollmentEntity;
import com.saracoglu.student.system.repository.CourseCatalogRepository;
import com.saracoglu.student.system.repository.CourseRegistrationRepository;
import com.saracoglu.student.system.service.mapper.StudentSystemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseCatalogService {

    @Autowired
    private CourseCatalogRepository courseCatalogRepository;

    @Autowired
    private StudentSystemMapper studentSystemMapper;

    @Autowired
    CourseRegistrationRepository courseRegistrationRepository;

    public CourseInfo addCourse(CourseInfo courseInfo) {
        // DTO -> Entity dönüşümü
        CourseCatalogEntity courseEntity = studentSystemMapper.convertToEntity(courseInfo);

        // Veritabanına kaydetme
        CourseCatalogEntity savedEntity = courseCatalogRepository.save(courseEntity);

        // Entity -> DTO dönüşümü
        return studentSystemMapper.convertToDto(savedEntity);
    }

    public CourseInfo getCourseById(Long id) {
        CourseCatalogEntity courseEntity = courseCatalogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        return studentSystemMapper.convertToDto(courseEntity);
    }

    public List<CourseInfo> getCourses() {
        List<CourseCatalogEntity> courseEntities = courseCatalogRepository.findAll();
        return courseEntities.stream()
                .map(studentSystemMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public void deleteCourseById(Long id) {
        // Verilen ID'ye sahip kurs var mı kontrol et
        if (!courseCatalogRepository.existsById(id)) {
            throw new RuntimeException("Course with ID " + id + " not found.");
        }
        // Kursu sil
        courseCatalogRepository.deleteById(id);
    }
    public List<StudentEnrollmentEntity> getStudentsByCourse(Long courseId) {
        List<CourseRegistrationEntity> registrations = courseRegistrationRepository.findByCourseId(courseId);
        // CourseRegistrationEntity'lerden öğrenci bilgilerini almak
        return registrations.stream()
                .map(CourseRegistrationEntity::getStudent)
                .collect(Collectors.toList());
    }
}
