package com.saracoglu.student.system.exception.handler;

import com.saracoglu.student.system.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // HttpMessageNotReadableException için mevcut handler
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Map<String, List<String>> errorsMap = new HashMap<>();
        errorsMap.put("error", Collections.singletonList("Geçersiz değer girişi: " + ex.getMessage()));

        ApiError apiError = createApiError(errorsMap);
        return ResponseEntity.badRequest().body(apiError);
    }

    // MethodArgumentNotValidException için yeni handler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errorsMap = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errorsMap.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
        });

        ApiError apiError = createApiError(errorsMap);
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiError> handleBindException(BindException ex) {
        Map<String, List<String>> errorsMap = new HashMap<>();

        ex.getFieldErrors().forEach(fieldError -> {
            String fieldName = fieldError.getField();
            String errorMessage = "Invalid value for field '" + fieldName + "': " + fieldError.getRejectedValue();
            errorsMap.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
        });

        ApiError apiError = createApiError(errorsMap);
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError> handleDataAccessException(DataAccessException ex) {
        Map<String, List<String>> errorsMap = new HashMap<>();
        errorsMap.put("database_error", Collections.singletonList("A database error occurred: " + ex.getMessage()));
        ApiError apiError = createApiError(errorsMap);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    // Öğrenci bulunamadığında fırlatılan istisna
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ApiError> handleStudentNotFoundException(StudentNotFoundException ex) {
        Map<String, List<String>> errorsMap = new HashMap<>();
        errorsMap.put("error", Collections.singletonList(ex.getMessage()));
        ApiError apiError = createApiError(errorsMap);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    // Departman bulunamadığında fırlatılan istisna
    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<ApiError> handleDepartmentNotFoundException(DepartmentNotFoundException ex) {
        Map<String, List<String>> errorsMap = new HashMap<>();
        errorsMap.put("error", Collections.singletonList(ex.getMessage()));
        ApiError apiError = createApiError(errorsMap);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    // MissingPathVariableException için handler
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ApiError> handleMissingPathVariableException(MissingPathVariableException ex) {
        Map<String, List<String>> errorsMap = new HashMap<>();
        errorsMap.put("error", Collections.singletonList("Path variable is missing: " + ex.getVariableName()));

        ApiError apiError = createApiError(errorsMap);
        return ResponseEntity.badRequest().body(apiError);
    }

    // MethodArgumentTypeMismatchException için handler
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        Map<String, List<String>> errorsMap = new HashMap<>();
        errorsMap.put("error", Collections.singletonList("Invalid type for path variable: " + ex.getName()));

        ApiError apiError = createApiError(errorsMap);
        return ResponseEntity.badRequest().body(apiError);
    }


    // Öğrenci yokken fırlatılan istisna
    @ExceptionHandler(NoStudentsFoundException.class)
    public ResponseEntity<ApiError> handleNoStudentsFoundException(NoStudentsFoundException ex) {
        Map<String, List<String>> errorsMap = new HashMap<>();
        errorsMap.put("error", Collections.singletonList(ex.getMessage()));
        ApiError apiError = createApiError(errorsMap);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    // Var olan bir öğrenci kaydı eklenmeye çalışıldığında fırlatılan istisna
    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex) {
        Map<String, List<String>> errorsMap = new HashMap<>();
        errorsMap.put("error", Collections.singletonList(ex.getMessage()));
        ApiError apiError = createApiError(errorsMap);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<ApiError> handleCourseNotFoundException(CourseNotFoundException ex) {
        Map<String, List<String>> errorsMap = new HashMap<>();
        errorsMap.put("error", Collections.singletonList(ex.getMessage()));
        ApiError apiError = createApiError(errorsMap);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    // NoCourseFoundException için handler
    @ExceptionHandler(NoCourseFoundException.class)
    public ResponseEntity<ApiError> handleNoCourseFoundException(NoCourseFoundException ex) {
        Map<String, List<String>> errorsMap = new HashMap<>();
        errorsMap.put("error", Collections.singletonList(ex.getMessage()));
        ApiError apiError = createApiError(errorsMap);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }


    // ApiError oluşturma metodu
    private ApiError createApiError(Map<String, List<String>> errors) {
        ApiError apiError = new ApiError();
        apiError.setId(UUID.randomUUID().toString());
        apiError.setErrorTime(new Date());
        apiError.setErrors(errors);
        return apiError;
    }
}