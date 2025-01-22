package com.saracoglu.student.system.contoller;

import com.saracoglu.student.system.dto.StudentInfo;
import com.saracoglu.student.system.service.StudentManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/students")
public class StudentManagementController {

    @Autowired
    private StudentManagementService studentManagementService;

    // Yeni bir öğrenci ekleme
    @PostMapping
    public StudentInfo addStudent(@Valid @RequestBody StudentInfo studentInfo) {
        return studentManagementService.addStudent(studentInfo);
    }

    // ID'ye göre öğrenci getirme
    @GetMapping("/{id}")
    public StudentInfo getStudentById(@PathVariable Long id) {
        return studentManagementService.findById(id);
    }

    // Tüm öğrencileri listeleme
    @GetMapping
    public List<StudentInfo> getAllStudents() {
        return studentManagementService.getStudents();
    }

    // ID'ye göre öğrenci silme
    @DeleteMapping("/{id}")
    public void deleteStudentById(@PathVariable Long id) {
        studentManagementService.deleteStudent(id);
    }
}
