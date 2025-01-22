package com.saracoglu.student.system.contoller;

import com.saracoglu.student.system.dto.DepartmentInfo;
import com.saracoglu.student.system.service.DepartmentCatalogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/departments")
public class DepartmentCatalogController {

    @Autowired
    private DepartmentCatalogService departmentCatalogService;

    // Yeni bir departman ekleme
    @PostMapping
    public DepartmentInfo addDepartment(@Valid @RequestBody DepartmentInfo departmentInfo) {
        return departmentCatalogService.addDepartment(departmentInfo);
    }

    // ID'ye göre departman getirme
    @GetMapping("/{id}")
    public DepartmentInfo getDepartmentById(@PathVariable Long id) {
        return departmentCatalogService.getDepartmentById(id);
    }

    // Tüm departmanları listeleme
    @GetMapping
    public List<DepartmentInfo> getAllDepartments() {
        return departmentCatalogService.getAllDepartments();
    }

    // ID'ye göre departman silme
    @DeleteMapping("/{id}")
    public void deleteDepartmentById(@PathVariable Long id) {
        departmentCatalogService.deleteDepartmentById(id);
    }
}
