package com.idemy.controller;

import com.idemy.dto.request.ReviewRequest;
import com.idemy.service.EngagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/engagement")
@RequiredArgsConstructor
public class EngagementController {

    private final EngagementService engagementService;

    // Rəy əlavə et
    @PostMapping("/reviews/{courseId}")
    public ResponseEntity<String> postReview(@PathVariable Long courseId, @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(engagementService.addReview(courseId, request));
    }

    // Dərsi bitir
    @PostMapping("/lessons/{lessonId}/complete")
    public ResponseEntity<String> complete(@PathVariable Long lessonId) {
        return ResponseEntity.ok(engagementService.completeLesson(lessonId));
    }
}