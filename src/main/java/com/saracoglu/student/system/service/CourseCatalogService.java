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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class CourseCatalogService implements Serializable {
    private static final long serialVersionUID = 2024013101L;

    @Autowired
    private CourseCatalogRepository courseCatalogRepository;

    @Autowired
    private StudentSystemMapper studentSystemMapper;

    @Autowired
    private CourseRegistrationRepository courseRegistrationRepository;

    @CachePut(value = "courses", key = "#result.id")
    public CourseInfo addCourse(CourseInfo courseInfo) {
        CourseCatalogEntity courseEntity = studentSystemMapper.convertToEntity(courseInfo);
        CourseCatalogEntity savedEntity = courseCatalogRepository.save(courseEntity);
        return studentSystemMapper.convertToDto(savedEntity);
    }

    @Cacheable(value = "courses", key = "#id")
    public CourseInfo getCourseById(Long id) {
        CourseCatalogEntity courseEntity = courseCatalogRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(format("%d id'li bir ders bulunamadı", id)));
        return studentSystemMapper.convertToDto(courseEntity);
    }

    @Cacheable(value = "allCourses")
    public List<CourseInfo> getCourses() {
        List<CourseCatalogEntity> courseEntities = courseCatalogRepository.findAll();
        if (courseEntities.isEmpty()) {
            throw new NoCourseFoundException("veritabanında hiç ders yok");
        }
        return courseEntities.stream()
                .map(studentSystemMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "courses", key = "#id")
    public void deleteCourseById(Long id) {
        if (!courseCatalogRepository.existsById(id)) {
            throw new CourseNotFoundException(format("%d id'li bir ders bulunamadı", id));
        }
        courseCatalogRepository.deleteById(id);
    }

    @Cacheable(value = "courseStudents", key = "#courseId")
    public List<StudentEnrollmentEntity> getStudentsByCourse(Long courseId) {
        if (!courseCatalogRepository.existsById(courseId)) {
            throw new CourseNotFoundException(format("%d id'li bir ders bulunamadı", courseId));
        }
        List<CourseRegistrationEntity> registrations = courseRegistrationRepository.findByCourseId(courseId);
        return registrations.stream()
                .map(CourseRegistrationEntity::getStudent)
                .collect(Collectors.toList());
    }
}