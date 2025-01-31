package com.saracoglu.student.system.contoller;

import com.saracoglu.student.system.dto.StudentInfo;
import com.saracoglu.student.system.service.StudentManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentManagementController {

    @Autowired
    private StudentManagementService studentManagementService;

    @PostMapping
    public StudentInfo addStudent(@Valid @RequestBody StudentInfo studentInfo) {
        return studentManagementService.addStudent(studentInfo);
    }

    @GetMapping("/{id}")
    public StudentInfo getStudentById(@PathVariable Long id) {
        return studentManagementService.findById(id);
    }

    @GetMapping
    public List<StudentInfo> getAllStudents() {
        return studentManagementService.getStudents();
    }

    @DeleteMapping("/{id}")
    public void deleteStudentById(@PathVariable Long id) {
        studentManagementService.deleteStudent(id);
    }
}
