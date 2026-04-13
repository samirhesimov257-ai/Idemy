package com.idemy.dto.request;

import lombok.Data;

@Data
public class LessonRequest {
    private String title;
    private String content;
    private Integer durationInMinutes;
    private Integer orderIndex;
}