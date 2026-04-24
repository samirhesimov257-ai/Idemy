package com.idemy.mapper;

import com.idemy.dao.entity.Lesson;
import com.idemy.dto.responce.LessonResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LessonMapper {
    public LessonResponse toDtoLessonRes(Lesson lesson){
        return LessonResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .content(lesson.getContent())
                .durationInMinutes(lesson.getDurationInMinutes())
                .orderIndex(lesson.getOrderIndex())
                .build();
    }

    public List<LessonResponse> toListDtoLessonRes (List<Lesson> lessons ){
        if (lessons == null) {
            throw new IllegalArgumentException("Lessons list cannot be null");
        }
        return lessons.stream()
                .map(this::toDtoLessonRes)
                .toList();

    }

}
