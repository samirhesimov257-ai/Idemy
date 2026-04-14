package com.idemy.controller;

import com.idemy.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @GetMapping("/{lessonId}/video")
    public ResponseEntity<Resource> streamVideo(@PathVariable Long lessonId) {
        Resource video = lessonService.getLessonVideoResource(lessonId);
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("video/mp4")) // Video formatı
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline") // Brauzerdə birbaşa açılması üçün
                .body(video);
    }
}