package com.saracoglu.student.system.repository;

import com.saracoglu.student.system.entity.CourseRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRegistrationRepository extends JpaRepository<CourseRegistrationEntity, Long> {
    List<CourseRegistrationEntity> findByCourseId(Long courseId);
}
