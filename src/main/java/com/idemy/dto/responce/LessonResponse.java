package com.idemy.dto.responce;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LessonResponse {
    private Long id;
    private String title;
    private String content;
    private String videoUrl;
    private Integer durationInMinutes;
    private Integer orderIndex;
}