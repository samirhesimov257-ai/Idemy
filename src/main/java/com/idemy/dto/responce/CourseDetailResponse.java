package com.idemy.dto.responce;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CourseDetailResponse {
    private Long id;
    private String title;
    private String description;
    private String instructorName;
    private List<SectionResponse> sections; // Sillabus buradadır
}