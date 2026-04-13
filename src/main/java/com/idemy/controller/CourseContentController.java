package com.idemy.controller;

import com.idemy.dto.request.SectionRequest;
import com.idemy.dto.responce.LessonResponse;
import com.idemy.dto.responce.SectionResponse;
import com.idemy.service.CourseContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/content")
@RequiredArgsConstructor
public class CourseContentController {

    private final CourseContentService contentService;

    @PostMapping("/courses/{courseId}/sections")
    public ResponseEntity<SectionResponse> createSection(
            @PathVariable Long courseId, 
            @RequestBody SectionRequest request) {
        return ResponseEntity.ok(contentService.addSection(courseId, request));
    }

    @PostMapping("/sections/{sectionId}/lessons")
    public ResponseEntity<LessonResponse> createLesson(
            @PathVariable Long sectionId,
            @RequestParam String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) Integer duration,
            @RequestParam(required = false) Integer orderIndex,
            @RequestParam("video") MultipartFile video) {
//        String fileUrl = "http://localhost:8080/uploads/videos/" + fileName;
//        System.out.println(fileUrl);
        
        return ResponseEntity.ok(contentService.addLesson(sectionId, title, content, duration, orderIndex, video));
    }
}