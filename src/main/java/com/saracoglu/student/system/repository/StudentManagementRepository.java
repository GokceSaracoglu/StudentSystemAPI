package com.saracoglu.student.system.repository;

import com.saracoglu.student.system.entity.StudentEnrollmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentManagementRepository extends JpaRepository<StudentEnrollmentEntity, Long> {
}
