package com.saracoglu.student.system.repository;

import com.saracoglu.student.system.entity.DepartmentCatalogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentCatalogRepository extends JpaRepository<DepartmentCatalogEntity, Long> {
}
