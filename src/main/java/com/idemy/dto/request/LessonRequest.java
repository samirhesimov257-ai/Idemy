package com.idemy.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;

@Data
public class LessonRequest {
    private String title;
    private String content;
    private Duration durationInMinutes;
    private Integer orderIndex;
    private MultipartFile video;
}