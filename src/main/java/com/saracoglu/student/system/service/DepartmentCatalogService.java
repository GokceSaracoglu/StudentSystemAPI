package com.saracoglu.student.system.service;

import com.saracoglu.student.system.dto.DepartmentInfo;
import com.saracoglu.student.system.entity.DepartmentCatalogEntity;
import com.saracoglu.student.system.repository.DepartmentCatalogRepository;
import com.saracoglu.student.system.service.mapper.StudentSystemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentCatalogService {

    @Autowired
    private DepartmentCatalogRepository departmentCatalogRepository;

    @Autowired
    private StudentSystemMapper studentSystemMapper;

    // Yeni bir departman ekleme
    public DepartmentInfo addDepartment(DepartmentInfo departmentInfo) {
        DepartmentCatalogEntity departmentEntity = studentSystemMapper.convertToEntity(departmentInfo);
        DepartmentCatalogEntity savedEntity = departmentCatalogRepository.save(departmentEntity);
        return studentSystemMapper.convertToDto(savedEntity);
    }

    // ID'ye göre departman getirme
    public DepartmentInfo getDepartmentById(Long id) {
        DepartmentCatalogEntity departmentEntity = departmentCatalogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        return studentSystemMapper.convertToDto(departmentEntity);
    }

    // Tüm departmanları listeleme
    public List<DepartmentInfo> getAllDepartments() {
        List<DepartmentCatalogEntity> departmentEntities = departmentCatalogRepository.findAll();
        return departmentEntities.stream()
                .map(studentSystemMapper::convertToDto)
                .collect(Collectors.toList());
    }

    // ID'ye göre departman silme
    public void deleteDepartmentById(Long id) {
        if (!departmentCatalogRepository.existsById(id)) {
            throw new RuntimeException("Department not found");
        }
        departmentCatalogRepository.deleteById(id);
    }
}
