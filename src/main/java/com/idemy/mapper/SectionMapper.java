package com.idemy.mapper;

import com.idemy.dao.entity.Section;
import com.idemy.dto.responce.SectionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SectionMapper {
    private final LessonMapper lessonMapper ;
    public SectionResponse toDtoRes(Section section ){
        return SectionResponse.builder()
                .id(section.getId())
                .title(section.getTitle())
//                .orderIndex(section.getOrderIndex())
                .lessons(lessonMapper.toListDtoLessonRes(section.getLessons()))
                .build();
    }

    public List<SectionResponse> toListDtoSectionRes(List<Section> sections ){
        if (sections == null) {
            throw new IllegalArgumentException("Lessons list cannot be null");
        }
        return sections.stream()
                .map(this::toDtoRes)
                .toList();
    }

}
