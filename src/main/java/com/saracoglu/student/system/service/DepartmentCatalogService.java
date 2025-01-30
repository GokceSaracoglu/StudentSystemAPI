package com.saracoglu.student.system.service;

import com.saracoglu.student.system.dto.DepartmentInfo;
import com.saracoglu.student.system.entity.DepartmentCatalogEntity;
import com.saracoglu.student.system.exception.DepartmentNotFoundException;
import com.saracoglu.student.system.repository.DepartmentCatalogRepository;
import com.saracoglu.student.system.service.mapper.StudentSystemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentCatalogService {

    @Autowired
    private DepartmentCatalogRepository departmentCatalogRepository;

    @Autowired
    private StudentSystemMapper studentSystemMapper;

    @CachePut(value = "departments", key = "#result.id")
    public DepartmentInfo addDepartment(DepartmentInfo departmentInfo) {
        DepartmentCatalogEntity departmentEntity = studentSystemMapper.convertToEntity(departmentInfo);
        DepartmentCatalogEntity savedEntity = departmentCatalogRepository.save(departmentEntity);
        return studentSystemMapper.convertToDto(savedEntity);
    }

    @Cacheable(value = "departments", key = "#id")
    public DepartmentInfo getDepartmentById(Long id) {
        DepartmentCatalogEntity departmentEntity = departmentCatalogRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found"));
        return studentSystemMapper.convertToDto(departmentEntity);
    }

    @Cacheable(value = "allDepartments")
    public List<DepartmentInfo> getAllDepartments() {
        List<DepartmentCatalogEntity> departmentEntities = departmentCatalogRepository.findAll();
        return departmentEntities.stream()
                .map(studentSystemMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "departments", key = "#id")
    public void deleteDepartmentById(Long id) {
        if (!departmentCatalogRepository.existsById(id)) {
            throw new DepartmentNotFoundException("Department not found");
        }
        departmentCatalogRepository.deleteById(id);
    }
}