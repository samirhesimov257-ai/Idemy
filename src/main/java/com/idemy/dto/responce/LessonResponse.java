package com.idemy.dto.responce;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;

@Data
@Builder
public class LessonResponse {
    private Long id;
    private String title;
    private String content;
//    private String videoUrl;
    private Duration durationInMinutes;
    private Integer orderIndex;
}