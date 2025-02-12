package com.saracoglu.student.system.exception.handler;

import com.saracoglu.student.system.exception.*;
import com.saracoglu.student.system.logging.LoggingHelper;
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
import jakarta.servlet.http.HttpServletRequest;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final LoggingHelper loggingHelper;

    public GlobalExceptionHandler(LoggingHelper loggingHelper) {
        this.loggingHelper = loggingHelper;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException ex) {
        return handleException(request, ex, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ColumnNotFoundException.class)
    public ResponseEntity<ApiError> handleColumnNotFoundException(HttpServletRequest request, ColumnNotFoundException ex) {
        return handleException(request, ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPageRequestException.class)
    public ResponseEntity<ApiError> handleInvalidPageRequestException(HttpServletRequest request, InvalidPageRequestException ex) {
        return handleException(request, ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.computeIfAbsent(error.getField(), k -> new ArrayList<>()).add(error.getDefaultMessage()));

        return handleException(request, ex, HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiError> handleBindException(HttpServletRequest request, BindException ex) {
        Map<String, List<String>> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.computeIfAbsent(error.getField(), k -> new ArrayList<>()).add(error.getDefaultMessage()));

        return handleException(request, ex, HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError> handleDataAccessException(HttpServletRequest request, DataAccessException ex) {
        return handleException(request, ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ApiError> handleStudentNotFoundException(HttpServletRequest request, StudentNotFoundException ex) {
        return handleException(request, ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<ApiError> handleDepartmentNotFoundException(HttpServletRequest request, DepartmentNotFoundException ex) {
        return handleException(request, ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoStudentsFoundException.class)
    public ResponseEntity<ApiError> handleNoStudentsFoundException(HttpServletRequest request, NoStudentsFoundException ex) {
        return handleException(request, ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleEntityAlreadyExistsException(HttpServletRequest request, EntityAlreadyExistsException ex) {
        return handleException(request, ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<ApiError> handleCourseNotFoundException(HttpServletRequest request, CourseNotFoundException ex) {
        return handleException(request, ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoCourseFoundException.class)
    public ResponseEntity<ApiError> handleNoCourseFoundException(HttpServletRequest request, NoCourseFoundException ex) {
        return handleException(request, ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ApiError> handleMissingPathVariableException(HttpServletRequest request, MissingPathVariableException ex) {
        return handleException(request, ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException(HttpServletRequest request, MethodArgumentTypeMismatchException ex) {
        Map<String, List<String>> errors = new HashMap<>();
        errors.put(ex.getName(), Collections.singletonList("Beklenen tip: " + ex.getRequiredType()));

        return handleException(request, ex, HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(HttpServletRequest request, Exception ex) {
        return handleException(request, ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiError> handleException(HttpServletRequest request, Exception ex, HttpStatus status) {
        return handleException(request, ex, status, Collections.singletonMap("error", Collections.singletonList(ex.getMessage())));
    }

    private ResponseEntity<ApiError> handleException(HttpServletRequest request, Exception ex, HttpStatus status, Map<String, List<String>> errors) {
        String requestId = UUID.randomUUID().toString();
        ApiError apiError = new ApiError(requestId, new Date(), errors);

        loggingHelper.logError(requestId, ex.getMessage(), ex);

        return ResponseEntity.status(status).body(apiError);
    }
}
