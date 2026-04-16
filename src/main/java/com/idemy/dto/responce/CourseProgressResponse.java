package com.idemy.dto.responce;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseProgressResponse {
    private double percentage;
    private long completedLessons;
    private long totalLessons;
}