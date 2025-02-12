package com.saracoglu.student.system.service;

import com.saracoglu.student.system.exception.ColumnNotFoundException;
import com.saracoglu.student.system.dto.StudentInfo;
import com.saracoglu.student.system.entity.StudentEnrollmentEntity;
import com.saracoglu.student.system.exception.InvalidPageRequestException;
import com.saracoglu.student.system.exception.StudentNotFoundException;
import com.saracoglu.student.system.repository.DepartmentCatalogRepository;
import com.saracoglu.student.system.repository.StudentManagementRepository;
import com.saracoglu.student.system.service.mapper.StudentSystemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class StudentManagementService implements Serializable {
    private static final long serialVersionUID = 2024013103L;

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
    public Page<StudentInfo> findAllPageable(Pageable pageable) {
        Page<StudentEnrollmentEntity> students = studentManagementRepository.findAll(pageable);

        if (students.getTotalPages() > 0 && pageable.getPageNumber() >= students.getTotalPages()) {
            throw new InvalidPageRequestException(
                    String.format("Geçersiz sayfa numarası: %d. Toplam sayfa sayısı: %d",
                            pageable.getPageNumber(), students.getTotalPages()));
        }

        return students.map(studentSystemMapper::convertToDto);
    }

    @CacheEvict(value = "students", key = "#studentId")
    public void deleteStudent(Long studentId) {
        if (!studentManagementRepository.existsById(studentId)) {
            throw new StudentNotFoundException(String.format("Öğrenci (%s) bulunamadı.", studentId));
        }
        studentManagementRepository.deleteById(studentId);
    }

    public void checkColumnExistence(String columnName) {
        // Bu, örnek olarak kullanılan bir metot. Gerçek sütun kontrolü yapılmalı.
        if (!columnExistsInDatabase(columnName)) {
            throw new ColumnNotFoundException("Veritabanında '" + columnName + "' adında bir sütun bulunamadı.");
        }
    }

    private boolean columnExistsInDatabase(String columnName) {
        // Bu, sadece örnek olarak yazılmıştır.
        // Gerçek veritabanı sorgusu ile sütunun varlığı kontrol edilmelidir.
        return false;
    }
}
