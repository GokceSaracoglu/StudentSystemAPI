package com.saracoglu.student.system.contoller;

import com.saracoglu.student.system.dto.StudentInfo;
import com.saracoglu.student.system.service.StudentManagementService;
import com.saracoglu.student.system.service.mapper.StudentSystemMapper;
import com.saracoglu.student.system.utils.PagerUtil;
import com.saracoglu.student.system.utils.RestPageableEntity;
import com.saracoglu.student.system.utils.RestPageableRequest;
import com.saracoglu.student.system.utils.RestRootEntity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/students")
public class StudentManagementController extends RestBaseController {

    @Autowired
    private StudentManagementService studentManagementService;

    @Autowired
    private StudentSystemMapper studentSystemMapper;

    @PostMapping
    public StudentInfo addStudent(@Valid @RequestBody StudentInfo studentInfo) {
        return studentManagementService.addStudent(studentInfo);
    }

    @GetMapping("/{id}")
    public StudentInfo getStudentById(@PathVariable Long id) {
        return studentManagementService.findById(id);
    }

    @GetMapping
    public RestRootEntity<RestPageableEntity<StudentInfo>> findAllPageable(RestPageableRequest pageable) {
        // Sayfalama nesnesini oluştur
        Pageable pageRequest = PagerUtil.toPageable(pageable);

        // Veritabanından sayfalı öğrenci verilerini getir
        Page<StudentInfo> page = studentManagementService.findAllPageable(pageRequest);

        // Sayfalama yanıtını oluştur
        RestPageableEntity<StudentInfo> pageableResponse = PagerUtil.toPageableResponse(page, page.getContent());

        // Yanıtı RestRootEntity içerisine sar ve döndür
        return RestRootEntity.ok(pageableResponse);
    }

    @DeleteMapping("/{id}")
    public void deleteStudentById(@PathVariable Long id) {
        studentManagementService.deleteStudent(id);
    }
}