package com.saracoglu.student.system.service;

import com.saracoglu.student.system.dto.StudentInfo;
import com.saracoglu.student.system.entity.StudentEnrollmentEntity;
import com.saracoglu.student.system.exception.StudentNotFoundException;
import com.saracoglu.student.system.repository.DepartmentCatalogRepository;
import com.saracoglu.student.system.repository.StudentManagementRepository;
import com.saracoglu.student.system.service.mapper.StudentSystemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class StudentManagementService {

    @Autowired
    private StudentManagementRepository studentManagementRepository;

    @Autowired
    private DepartmentCatalogRepository departmentCatalogRepository;

    @Autowired
    private StudentSystemMapper studentSystemMapper;

    @CachePut(value = "students", key = "#result.id")
    public StudentInfo addStudent(StudentInfo studentInfo) {
        StudentEnrollmentEntity studentEntity = studentSystemMapper.convertToEntity(studentInfo);
        if (studentInfo.getDepartmentId() != null) {
            studentEntity.setDepartment(
                    departmentCatalogRepository.findById(studentInfo.getDepartmentId())
                            .orElseThrow(() -> new RuntimeException("Department not found"))
            );
        }
        StudentEnrollmentEntity savedEntity = studentManagementRepository.save(studentEntity);
        return studentSystemMapper.convertToDto(savedEntity);
    }

    @Cacheable(value = "students", key = "#id")
    public StudentInfo findById(Long id) {
        StudentEnrollmentEntity studentEntity = studentManagementRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(String.format("Öğrenci (%s) bulunamadı.", id)));
        return studentSystemMapper.convertToDto(studentEntity);
    }

    @Cacheable(value = "allStudents")
    public List<StudentInfo> getStudents() {
        List<StudentEnrollmentEntity> students = studentManagementRepository.findAll();
        return students.stream()
                .map(studentSystemMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "students", key = "#studentId")
    public void deleteStudent(Long studentId) {
        if (!studentManagementRepository.existsById(studentId)) {
            throw new StudentNotFoundException(String.format("Öğrenci (%s) bulunamadı.", studentId));
        }
        studentManagementRepository.deleteById(studentId);
    }
}