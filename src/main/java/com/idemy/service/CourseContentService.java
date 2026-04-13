package com.idemy.service;

import com.idemy.dao.entity.Course;
import com.idemy.dao.entity.Lesson;
import com.idemy.dao.entity.Section;
import com.idemy.dao.repository.CourseRepository;
import com.idemy.dao.repository.LessonRepository;
import com.idemy.dao.repository.SectionRepository;
import com.idemy.dto.request.SectionRequest;
import com.idemy.dto.responce.LessonResponse;
import com.idemy.dto.responce.SectionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CourseContentService {

    private final SectionRepository sectionRepository;
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final FileService fileService;

    // BÖLMƏ ƏLAVƏ ETMƏK
    public SectionResponse addSection(Long courseId, SectionRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Kurs tapılmadı"));
        
        checkOwnership(course);

        int order = (request.getOrderIndex() != null) ? request.getOrderIndex() :
                     (int) sectionRepository.countByCourseId(courseId) + 1;

        Section section = new Section();
        section.setTitle(request.getTitle());
        section.setOrderIndex(order);
        section.setCourse(course);

        return mapToSectionResponse(sectionRepository.save(section));
    }

    // DƏRS ƏLAVƏ ETMƏK (VİDEO İLE)
    public LessonResponse addLesson(Long sectionId, String title, String content,
                                    Integer duration, Integer orderIndex, MultipartFile video) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Bölmə tapılmadı"));
        
        checkOwnership(section.getCourse());

        // Videonu lokal yaddaşa yazırıq
        String videoFileName = fileService.saveFile(video);

        int order = (orderIndex != null) ? orderIndex : 
                     (int) lessonRepository.countBySectionId(sectionId) + 1;

        Lesson lesson = new Lesson();
        lesson.setTitle(title);
        lesson.setContent(content);
        lesson.setDurationInMinutes(duration);
        lesson.setVideoUrl(videoFileName);
        lesson.setOrderIndex(order);
        lesson.setSection(section);

        return mapToLessonResponse(lessonRepository.save(lesson));
    }

    // MAPPING METODLARI
    private SectionResponse mapToSectionResponse(Section section) {
        return SectionResponse.builder()
                .id(section.getId())
                .title(section.getTitle())
                .orderIndex(section.getOrderIndex())
                .lessons(section.getLessons() != null ? 
                         section.getLessons().stream().map(this::mapToLessonResponse).toList() : 
                         new ArrayList<>())
                .build();
    }

    private LessonResponse mapToLessonResponse(Lesson lesson) {
        return LessonResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .content(lesson.getContent())
                .durationInMinutes(lesson.getDurationInMinutes())
                .videoUrl(lesson.getVideoUrl())
                .orderIndex(lesson.getOrderIndex())
                .build();
    }

    private void checkOwnership(Course course) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!course.getInstructor().getEmail().equals(email)) {
            throw new RuntimeException("Bu kurs sizin deyil!");
        }
    }
}