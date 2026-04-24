package com.idemy.controller;

import com.idemy.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
@Operation(summary = "Kursdan qeydiyyatdan kecmek ucun")
    @PostMapping("/{courseId}")
    public ResponseEntity<String> enroll(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.enrollInCourse(courseId));
    }
}