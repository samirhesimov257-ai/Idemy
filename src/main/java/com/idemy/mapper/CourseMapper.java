package com.idemy.mapper;

import com.idemy.dao.entity.Course;
import com.idemy.dao.repository.EnrollmentRepository;
import com.idemy.dto.responce.CourseDetailResponse;
import com.idemy.dto.responce.CourseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CourseMapper {
    private final EnrollmentRepository enrollmentRepository;
    private final SectionMapper sectionMapper ;

    public CourseDetailResponse toDtoDetail(Course course){
        return CourseDetailResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .instructorName(course.getDescription())
                .sections(sectionMapper.toListDtoSectionRes(course.getSections()))
                .build();
    }

    public CourseResponse toDto(Course course ){
        long studentCount = enrollmentRepository.countByCourseId(course.getId());

        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .price(course.getPrice())
                .instructorName(course.getInstructor().getFullName())
                .studentCount(studentCount)
                .build();

    }
    public List<CourseResponse> listToDtoList(List<Course> courseList){
       return courseList.stream()
                .map(this::toDto)
                .toList();
    }

}
