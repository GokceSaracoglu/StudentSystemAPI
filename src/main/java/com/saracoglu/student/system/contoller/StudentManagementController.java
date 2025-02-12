package com.saracoglu.student.system.contoller;

import com.saracoglu.student.system.dto.StudentInfo;
import com.saracoglu.student.system.service.StudentManagementService;
import com.saracoglu.student.system.service.mapper.StudentSystemMapper;
import com.saracoglu.student.system.utils.PagerUtil;
import com.saracoglu.student.system.utils.RestPageableEntity;
import com.saracoglu.student.system.utils.RestPageableRequest;
import com.saracoglu.student.system.utils.RestRootEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@Validated
public class StudentManagementController {

    @Autowired
    private StudentManagementService studentManagementService;

    @Autowired
    private StudentSystemMapper studentSystemMapper;

    @PostMapping
    public StudentInfo addStudent(@Valid @RequestBody StudentInfo studentInfo) {
        return studentManagementService.addStudent(studentInfo);
    }

    @GetMapping("/{id}")
    public StudentInfo getStudentById(@PathVariable @Min(1) Long id) {
        return studentManagementService.findById(id);
    }

    @GetMapping
    public RestRootEntity<RestPageableEntity<StudentInfo>> findAllPageable(RestPageableRequest pageable) {
        Pageable pageRequest = PagerUtil.toPageable(pageable);
        Page<StudentInfo> page = studentManagementService.findAllPageable(pageRequest);
        RestPageableEntity<StudentInfo> pageableResponse = PagerUtil.toPageableResponse(page, page.getContent());
        return RestRootEntity.ok(pageableResponse);
    }

    @DeleteMapping("/{id}")
    public void deleteStudentById(@PathVariable @Min(1) Long id) {
        studentManagementService.deleteStudent(id);
    }
}
