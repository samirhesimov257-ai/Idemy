package com.idemy.dto.responce;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SectionResponse {
    private Long id;
    private String title;
//    private Integer orderIndex;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<LessonResponse> lessons;
}