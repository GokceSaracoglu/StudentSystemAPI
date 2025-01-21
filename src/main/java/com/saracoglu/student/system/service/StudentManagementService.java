package com.saracoglu.student.system.service;

import com.saracoglu.student.system.dto.StudentInfo;
import com.saracoglu.student.system.entity.StudentEnrollmentEntity;
import com.saracoglu.student.system.repository.DepartmentCatalogRepository;
import com.saracoglu.student.system.repository.StudentManagementRepository;
import com.saracoglu.student.system.service.mapper.StudentSystemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentManagementService {

    @Autowired
    private StudentManagementRepository studentManagementRepository;

    @Autowired
    private DepartmentCatalogRepository departmentCatalogRepository;

    @Autowired
    private StudentSystemMapper studentSystemMapper;

    public StudentInfo addStudent(StudentInfo studentInfo) {
        // DTO -> Entity dönüşümü
        StudentEnrollmentEntity studentEntity = studentSystemMapper.convertToEntity(studentInfo);

        // Department bilgisi ekleme
        if (studentInfo.getDepartmentId() != null) {
            studentEntity.setDepartment(
                    departmentCatalogRepository.findById(studentInfo.getDepartmentId())
                            .orElseThrow(() -> new RuntimeException("Department not found"))
            );
        }

        // Veritabanına kaydetme
        StudentEnrollmentEntity savedEntity = studentManagementRepository.save(studentEntity);

        // Entity -> DTO dönüşümü
        return studentSystemMapper.convertToDto(savedEntity);
    }

    public StudentInfo findById(Long id) {
        StudentEnrollmentEntity studentEntity = studentManagementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        return studentSystemMapper.convertToDto(studentEntity);
    }
    // Get All Students
    public List<StudentInfo> getStudents() {
        List<StudentEnrollmentEntity> students = studentManagementRepository.findAll();
        return students.stream()
                .map(studentSystemMapper::convertToDto)
                .collect(Collectors.toList());
    }

    // Delete Student by ID
    public void deleteStudent(Long studentId) {
        if (!studentManagementRepository.existsById(studentId)) {
            throw new RuntimeException("Student with ID " + studentId + " not found.");
        }
        studentManagementRepository.deleteById(studentId);
    }
}
