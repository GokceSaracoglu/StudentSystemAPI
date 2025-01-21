package com.saracoglu.student.system.repository;

import com.saracoglu.student.system.entity.CourseCatalogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseCatalogRepository extends JpaRepository<CourseCatalogEntity, Long> {

}
