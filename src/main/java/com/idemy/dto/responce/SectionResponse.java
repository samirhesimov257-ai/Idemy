package com.idemy.dto.responce;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SectionResponse {
    private Long id;
    private String title;
    private Integer orderIndex;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<LessonResponse> lessons; // Bölmənin daxilindəki dərslər
}