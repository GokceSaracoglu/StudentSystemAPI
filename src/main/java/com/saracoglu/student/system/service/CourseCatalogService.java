package com.saracoglu.student.system.service;

import com.saracoglu.student.system.dto.CourseInfo;
import com.saracoglu.student.system.entity.CourseCatalogEntity;
import com.saracoglu.student.system.entity.CourseRegistrationEntity;
import com.saracoglu.student.system.entity.StudentEnrollmentEntity;
import com.saracoglu.student.system.exception.CourseNotFoundException;
import com.saracoglu.student.system.exception.NoCourseFoundException;
import com.saracoglu.student.system.repository.CourseCatalogRepository;
import com.saracoglu.student.system.repository.CourseRegistrationRepository;
import com.saracoglu.student.system.service.mapper.StudentSystemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

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
                .orElseThrow(() -> new  CourseNotFoundException(format("%d id'li bir ders bulunamadı", id)));
        return studentSystemMapper.convertToDto(courseEntity);
    }

    public List<CourseInfo> getCourses() {

        List<CourseCatalogEntity> courseEntities = courseCatalogRepository.findAll();
        if (courseEntities.isEmpty()) {
            throw new NoCourseFoundException("veritabanında hiç ders yok");
        }
        return courseEntities.stream()
                .map(studentSystemMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public void deleteCourseById(Long id) {
        // Verilen ID'ye sahip kurs var mı kontrol et
        if (!courseCatalogRepository.existsById(id)) {
            throw new  CourseNotFoundException(format("%d id'li bir ders bulunamadı", id));
        }
        // Kursu sil
        courseCatalogRepository.deleteById(id);
    }
    public List<StudentEnrollmentEntity> getStudentsByCourse(Long courseId) {
        if (!courseCatalogRepository.existsById(courseId)) {
            throw new CourseNotFoundException(format("%d id'li bir ders bulunamadı", courseId));}
        List<CourseRegistrationEntity> registrations = courseRegistrationRepository.findByCourseId(courseId);
        // CourseRegistrationEntity'lerden öğrenci bilgilerini almak
        return registrations.stream()
                .map(CourseRegistrationEntity::getStudent)
                .collect(Collectors.toList());
    }
}
